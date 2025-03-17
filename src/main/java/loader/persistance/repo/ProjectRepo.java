package loader.persistance.repo;

import com.arcadedb.database.Record;
import com.arcadedb.graph.MutableVertex;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
public class ProjectRepo implements VersionedRepository<String, Project>
{
    private final static Logger LOG = LoggerFactory.getLogger(ProjectRepo.class);

    private final static String LATEST_STATE = "latest_project_state";

    private final static String HAS_STATE = "has_project_state";

    @Inject
    Database database;

    public Vertex init(String key)
    {
        Optional<Vertex> exists = exists(key);
        if(exists.isEmpty())
        {
            try(RemoteDatabase remoteDB = database.get())
            {
                remoteDB.begin();
                MutableVertex project = remoteDB.newVertex("project_id").set("key", key).set("count", 0).save();
                remoteDB.commit();
                LOG.info("Initialized project '{}'", key);
                return project;
            }
        } else
        {
            return exists.get();
        }
    }

    @Override
    public Optional<Vertex> exists(String key) {
        String sql = "SELECT FROM project_id WHERE key = :key;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command(Database.SQL, sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("key", key)));
            if(rs.hasNext())
            {
                return rs.next().getVertex();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Project> findById(String key)
    {
        Optional<Vertex> x = exists(key);
        if(x.isPresent()) {
            String sql = String.format("SELECT expand(@in) FROM %s WHERE @out = :out;", LATEST_STATE);
            try(RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getIdentity())));
                return resultSetToOptionalProject(rs);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        throw new NotImplementedYet();
    }

    @Override
    public Project persist(Project project)
    {
        Vertex versionedProject = init(project.getKey());
        try(RemoteDatabase remoteDB = database.get())
        {
            remoteDB.begin();
            Vertex projectState = remoteDB.newVertex("project")
                .set("key", project.getKey())
                .set("name", project.getName())
                .set("description", project.getDescription())
                .set("inserted_at", project.getInsertedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .save();
            Vertex version = remoteDB.lookupByRID(versionedProject.getIdentity()).asVertex();
            version.newEdge(HAS_STATE, projectState, false);
            reconnectLatestState(version, projectState);
            remoteDB.commit();
            LOG.info("Persisted state of project '{}'", project.getKey());
        }
        return project;
    }

    @Override
    public Project delete(Project entity) {
        throw new NotImplementedYet();
    }

    private Optional<Project> resultSetToOptionalProject(ResultSet resultSet)
    {
        if(resultSet.hasNext())
        {
            Result result = resultSet.next();
            if(result.getIdentity().isPresent())
            {
                Project project =  new Project();
                project.setRid(result.getIdentity().get().toString());
                project.setKey(result.getProperty("key"));
                project.setName(result.getProperty("name"));
                project.setDescription(result.getProperty("description"));
                project.setInsertedAt(ZonedDateTime.parse(result.getProperty("inserted_at")));
                return Optional.of(project);
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
