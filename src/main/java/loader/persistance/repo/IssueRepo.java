package loader.persistance.repo;

import com.arcadedb.database.Record;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Issue;
import loader.persistance.Project;
import org.jboss.resteasy.reactive.common.NotImplementedYet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class IssueRepo implements VersionedRepository<String, Issue>
{
    private static final Logger LOG = LoggerFactory.getLogger(IssueRepo.class);

    private final static String LATEST_STATE = "latest_issue_state";

    private final static String HAS_STATE = "has_issue_state";

    @Inject
    Database database;

    @Inject
    ProjectRepo projectRepo;

    public Vertex init(String issueKey, String projectKey)
    {
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
        } else
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
        throw new NotImplementedYet();
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
                .set("inserted_at", entity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .save();
            Vertex base = remoteDB.lookupByRID(versionedIssue.getIdentity()).asVertex();
            base.newEdge(HAS_STATE, issueState, false);
            reconnectLatestState(base, issueState);
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
                issue.setRid(result.getIdentity().get().toString());
                issue.setKey(result.getProperty("key"));
                issue.setProjectKey(result.getProperty("project_key"));
                issue.setSummary(result.getProperty("summary"));
                issue.setDescription(result.getProperty("description"));
                return Optional.of(issue);
            }
        }
        return Optional.empty();
    }

    private void reconnectLatestState(Vertex base, Vertex latestState)
    {
        base.getEdges(Vertex.DIRECTION.OUT, LATEST_STATE).forEach(Record::delete);
        base.newEdge(LATEST_STATE, latestState, true);
    }
}
