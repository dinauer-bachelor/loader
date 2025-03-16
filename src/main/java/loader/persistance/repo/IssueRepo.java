package loader.persistance.repo;

import com.arcadedb.database.RID;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.inject.Inject;
import loader.persistance.Issue;
import loader.persistance.VersionedEntity;
import org.apache.kafka.common.protocol.types.Field;
import org.jboss.resteasy.reactive.common.NotImplementedYet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IssueRepo implements VersionedRepository<String, Issue>
{
    private static final Logger LOG = LoggerFactory.getLogger(IssueRepo.class);

    @Inject
    Database database;

    @Override
    public VersionedEntity<String, Issue> init(String id)
    {
        Optional<VersionedEntity<String, Issue>> exists = exists(id);
        if(exists.isEmpty())
        {
            try(RemoteDatabase remoteDB = database.get())
            {
                String sql = "INSERT INTO issue_id(id) VALUES(:id)";
                ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("id", id)));
                LOG.info("Successfully initialized issue {}", id);
                if(rs.hasNext())
                {
                    Optional<RID> rid = rs.next().getIdentity();
                    if(rid.isPresent())
                    {
                        return new VersionedEntity<>();
                    }
                }
                throw new RuntimeException(String.format("Failed to initialize project %s", id));
            }
        } else
        {
            LOG.info("Issue {} already exists", id);
            return exists.get();
        }
    }

    @Override
    public Optional<VersionedEntity<String, Issue>> exists(String key) {
        String sql = "SELECT * FROM issue_id WHERE id = :id;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("id", key)));
            if(rs.hasNext())
            {
                return Optional.empty();
            } else
            {
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Issue> findById(String key)
    {
        throw new NotImplementedYet();
    }

    @Override
    public List<Issue> findAll()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Issue persist(Issue entity)
    {
        throw new NotImplementedYet();
    }

    @Override
    public Issue delete(Issue entity)
    {
        throw new NotImplementedYet();
    }
}
