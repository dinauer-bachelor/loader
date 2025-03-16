package loader.persistance.repo;

import com.arcadedb.database.RID;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.VersionedEntity;
import org.apache.kafka.common.protocol.types.Field;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.AbstractMap;
import java.util.Map;

public class EdgeRepository<E>
{
    RemoteDatabase remoteDB;

    public EdgeRepository(RemoteDatabase remoteDB)
    {
        this.remoteDB = remoteDB;
    }

    public void create(String edge, String from, String to)
    {
        String sql = String.format("CREATE EDGE %s FROM :from TO :to", edge);
        ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("from", from), new AbstractMap.SimpleEntry<>("to", to)));
        if (!rs.hasNext())
        {
            throw new RuntimeException(String.format("Failed to persist edge from %s to %s", from, to));
        }
    }

    public void delete(String edge, VersionedEntity<?, E> versionedEntity)
    {
        remoteDB.command("sql", String.format("DELETE FROM %s WHERE @out = :out;", edge), Map.ofEntries(new AbstractMap.SimpleEntry<>("out", versionedEntity.getRid())));
    }

    public void reconnect(String edge, VersionedEntity<?, E> versionedEntity, String to)
    {
        delete(edge, versionedEntity);
        create(edge, versionedEntity.getRid(), to);
    }


    public void find(String outgoing)
    {
        String sql = "SELECT * FROM latest_project_state WHERE @out = :out;";
        ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("out", outgoing)));
        if(rs.hasNext())
        {
            throw new NotImplementedYet();
        }
    }
}
