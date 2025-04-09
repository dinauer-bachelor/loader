package loader.persistance.repo;

import com.arcadedb.database.Record;
import com.arcadedb.graph.Edge;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.similarity.SimilarityCalculator;
import loader.persistance.Issue;
import loader.persistance.VersionedEntity;
import org.jboss.resteasy.reactive.common.NotImplementedYet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class IssueRepo implements VersionedRepository<String, Issue>
{
    private static final Logger LOG = LoggerFactory.getLogger(IssueRepo.class);

    private final static String VERTEX_TYPE = "issue";

    private final static String LATEST_STATE = "latest_issue_state";

    private final static String HAS_STATE = "has_issue_state";

    private final static String IS_SIMILAR_TO = "is_similar_to";

    @Inject
    Database database;

    @Inject
    ProjectRepo projectRepo;

    public Vertex init(String issueKey, String projectKey)
    {
        if(issueKey == null || projectKey == null)
        {
            throw new IllegalArgumentException("Issue key or project key cannot be null.");
        }
        Optional<Vertex> exists = exists(issueKey);
        if(exists.isEmpty())
        {
            try(RemoteDatabase remoteDB = database.get())
            {
                remoteDB.begin();
                Vertex issue = remoteDB.newVertex("issue_id").set("key", issueKey).save();
                Vertex project = remoteDB.lookupByRID(projectRepo.init(projectKey).getIdentity()).asVertex(); // Initialized and retrieves the corresponding project for creating its edge
                project.newEdge("belongs_to_project", issue, false).save();
                remoteDB.commit();
                LOG.info("Initialized issue '{}'", issueKey);
                return issue;
            }
        }
        else
        {
            return exists.get();
        }
    }

    @Override
    public Optional<Vertex> exists(String key) {
        String sql = "SELECT FROM issue_id WHERE key = :key;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("key", key)));
            if(rs.hasNext())
            {
                return rs.next().getVertex();
            } else
            {
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Issue> findById(String key)
    {
        Optional<Vertex> x = exists(key);
        if(x.isPresent()) {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", LATEST_STATE);
            try(RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getIdentity())));
                return resultSetToOptionalIssue(rs);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Issue> findAll()
    {
        return findAllVersions().stream().map(VersionedEntity::getLatest).toList();
    }

    @Override
    public List<VersionedEntity<String, Issue>> findAllVersions()
    {
        String sql = "SELECT FROM issue_id";
        try(RemoteDatabase remoteDB = database.get())
        {
            LOG.info("Executing SQL {}", sql);
            List<Result> result = remoteDB.command(Database.SQL, sql).stream().toList();
            List<VersionedEntity<String, Issue>> versionedIssues = new LinkedList<>();
            for(Result r : result)
            {
                if(r.getVertex().isPresent())
                {
                    Vertex vertex = r.getVertex().get();
                    VersionedEntity<String, Issue> issueVersionWrapper = new VersionedEntity<>();
                    issueVersionWrapper.setRid(vertex.getIdentity());
                    issueVersionWrapper.setId(vertex.getString("key"));
                    Iterator<Edge> outgoingEdges = vertex.getEdges(Vertex.DIRECTION.OUT, LATEST_STATE).iterator();
                    if(outgoingEdges.hasNext())
                    {
                        Vertex issueVertex = outgoingEdges.next().getInVertex();
                        Optional<Issue> optionalIssue = vertexToIssue(issueVertex);
                        if(optionalIssue.isPresent())
                        {
                            issueVersionWrapper.setLatest(optionalIssue.get());
                            versionedIssues.add(issueVersionWrapper);
                        }
                    }
                }
            }
            return versionedIssues;
        }
    }

    @Override
    public Issue persist(Issue entity)
    {
        Vertex versionedIssue = init(entity.getKey(), entity.getProjectKey());
        try(RemoteDatabase remoteDB = database.get())
        {
            remoteDB.begin();
            Vertex issueState = remoteDB.newVertex("issue")
                .set("key", entity.getKey())
                .set("project_key", entity.getProjectKey())
                .set("summary", entity.getSummary())
                .set("description", entity.getDescription())
                .set("status", entity.getStatus())
                .set("issuetype", entity.getIssuetype())
                .set("assignee", entity.getAssignee())
                .set("reporter", entity.getReporter())
                .set("inserted_at", entity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .save();
            entity.setRid(issueState.getIdentity());
            Vertex base = remoteDB.lookupByRID(versionedIssue.getIdentity()).asVertex();
            base.newEdge(HAS_STATE, issueState, false);
            reconnectLatestState(base, issueState);
            connectToSimilarIssues(remoteDB, versionedIssue, entity);
            remoteDB.commit();
            LOG.info("Persisted state of issue '{}'", entity.getKey());
        }
        return entity;
    }

    @Override
    public Issue delete(Issue entity)
    {
        throw new NotImplementedYet();
    }

    private Optional<Issue> resultSetToOptionalIssue(ResultSet resultSet)
    {
        if(resultSet.hasNext())
        {
            Result result = resultSet.next();
            if(result.getIdentity().isPresent())
            {
                Issue issue =  new Issue();
                issue.setRid(result.getIdentity().get());
                issue.setKey(result.getProperty("key"));
                issue.setProjectKey(result.getProperty("project_key"));
                issue.setSummary(result.getProperty("summary"));
                issue.setDescription(result.getProperty("description"));
                return Optional.of(issue);
            }
        }
        return Optional.empty();
    }

    private Optional<Issue> vertexToIssue(Vertex vertex)
    {
        if(VERTEX_TYPE.equals(vertex.getTypeName()))
        {
            Issue issue =  new Issue();
            issue.setKey(vertex.getString("key"));
            issue.setProjectKey(vertex.getString("project_key"));
            issue.setSummary(vertex.getString("summary"));
            issue.setDescription(vertex.getString("description"));
            issue.setStatus(vertex.getString("status"));
            issue.setIssuetype(vertex.getString("issuetype"));
            issue.setAssignee(vertex.getString("assignee"));
            issue.setReporter(vertex.getString("reporter"));
            return Optional.of(issue);
        }
        return Optional.empty();
    }

    private Issue resultToIssue(Result result)
    {
        Issue issue =  new Issue();
        issue.setKey(result.getProperty("key"));
        issue.setProjectKey(result.getProperty("project_key"));
        issue.setSummary(result.getProperty("summary"));
        issue.setDescription(result.getProperty("description"));
        issue.setStatus(result.getProperty("status"));
        issue.setIssuetype(result.getProperty("issuetype"));
        issue.setAssignee(result.getProperty("assignee"));
        issue.setReporter(result.getProperty("reporter"));
        return issue;
    }

    private void reconnectLatestState(Vertex base, Vertex latestState)
    {
        base.getEdges(Vertex.DIRECTION.OUT, LATEST_STATE).forEach(Record::delete);
        base.newEdge(LATEST_STATE, latestState, true);
    }

    private void connectToSimilarIssues(RemoteDatabase remoteDB, Vertex from, Issue vertex)
    {
        Vertex currentIssue = remoteDB.lookupByRID(from.getIdentity()).asVertex();
        cleanSimilarities(currentIssue);

        List<VersionedEntity<String, Issue>> versionedEntities = findAllVersions();
        for(VersionedEntity<String, Issue> version : versionedEntities)
        {
            if(version.getLatest() != null)
            {
                Optional<Double> similarityOptional = SimilarityCalculator.compare(version.getLatest(), vertex);
                if(similarityOptional.isPresent())
                {
                    Double similarity = similarityOptional.get();
                    Vertex otherIssues = remoteDB.lookupByRID(version.getRid()).asVertex();
                    if(!currentIssue.getIdentity().equals(otherIssues.getIdentity()))
                    {
                        currentIssue.newEdge(IS_SIMILAR_TO, otherIssues, true).set("similarity", similarity).save();
                        otherIssues.newEdge(IS_SIMILAR_TO, currentIssue, true).set("similarity", similarity).save();
                    }
                }

            }
        }
    }

    private void cleanSimilarities(Vertex from)
    {
        from.getEdges(Vertex.DIRECTION.BOTH, IS_SIMILAR_TO).forEach(Record::delete);
    }
}
