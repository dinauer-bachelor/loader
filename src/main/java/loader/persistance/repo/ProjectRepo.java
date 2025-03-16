package loader.persistance.repo;

import com.arcadedb.database.RID;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Project;
import loader.persistance.VersionedEntity;
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

    @Inject
    Database database;

    @Override
    public VersionedEntity<String, Project> init(String id)
    {
        Optional<VersionedEntity<String, Project>> exists = exists(id);
        if(exists.isEmpty())
        {
            try(RemoteDatabase remoteDB = database.get())
            {
                String sql = "INSERT INTO project_id(id, count) VALUES(:id, :count)";
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("id", id), new AbstractMap.SimpleEntry<>("count", 0)));
                LOG.info("Successfully initialized project {}", id);
                if(rs.hasNext())
                {
                    Result result = rs.next();
                    Optional<RID> rid = result.getIdentity();
                    if(rid.isPresent())
                    {
                        VersionedEntity<String, Project> entity = new VersionedEntity<>();
                        entity.setId(result.getProperty("id"));
                        entity.setRid(rid.get().toString());
                        entity.setCount(result.getProperty("count"));
                        entity.setLatest(null);
                        return entity;
                    }
                }
                throw new RuntimeException(String.format("Failed to initialize project %s", id));
            }
        } else
        {
            LOG.info("Project {} already exists", id);
            return exists.get();
        }
    }

    @Override
    public Optional<VersionedEntity<String, Project>> exists(String key) {
        String sql = "SELECT * FROM project_id WHERE id = :id;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("id", key)));
            if(rs.hasNext())
            {
                Result result = rs.next();
                if(result.getIdentity().isPresent())
                {
                    VersionedEntity<String, Project> entity = new VersionedEntity<>();
                    entity.setRid(result.getIdentity().get().toString());
                    entity.setCount(result.getProperty("count"));
                    entity.setLatest(null);
                    return Optional.of(entity);
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Project> findById(String key)
    {
        Optional<VersionedEntity<String, Project>> x = exists(key);
        if(x.isPresent()) {
            String sql = "SELECT expand(@in) FROM latest_project_state WHERE @out = :out;";
            try(RemoteDatabase remoteDB = database.get())
            {
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", x.get().getRid())));
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
        VersionedEntity<String, Project> versionedProject = init(project.getKey());
        String sql = "INSERT INTO project(key, name, description, inserted_at) VALUES(:key, :name, :description, :inserted_at);";
        try(RemoteDatabase remoteDB = database.get())
        {
            remoteDB.begin();
            ResultSet rs = remoteDB.command("sql", sql, projectToParams(project));
            if(rs.hasNext())
            {
                Result result = rs.next();
                if(result.getIdentity().isPresent())
                {
                    String rid = result.getIdentity().get().toString();
                    EdgeRepository<Project> edgeRepository = new EdgeRepository<>(remoteDB);
                    edgeRepository.create("has_project_state", rid, versionedProject.getRid());
                    edgeRepository.reconnect("latest_project_state", versionedProject, rid);
                    remoteDB.commit();
                    LOG.info("Successfully persisted new version of project {}", project.getKey());
                    return project;
                }
            }
            remoteDB.rollback();
            throw new RuntimeException(String.format("Failed to persist project %s", project.getKey()));
        }
    }

    @Override
    public Project delete(Project entity) {
        throw new NotImplementedYet();
    }

    private Map<String, String> projectToParams(Project project)
    {
        return Map.ofEntries(
            new AbstractMap.SimpleEntry<>("key", project.getKey()),
            new AbstractMap.SimpleEntry<>("name", project.getName()),
            new AbstractMap.SimpleEntry<>("description", project.getDescription()),
            new AbstractMap.SimpleEntry<>("inserted_at", project.getInsertedAt().format(DateTimeFormatter.ISO_DATE_TIME))
        );
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
}
