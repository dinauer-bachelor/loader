package loader.persistance.repo;

import com.arcadedb.database.RID;
import com.arcadedb.graph.Vertex;
import loader.persistance.VersionedEntity;

import java.util.List;
import java.util.Optional;

public interface VersionedRepository<K, E> extends Repository<K, E>
{
    Optional<Vertex> exists(K key);
    List<VersionedEntity<K, E>> findAllVersions();
}
