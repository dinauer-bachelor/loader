package loader.persistance.repo;

import com.arcadedb.database.RID;
import loader.persistance.VersionedEntity;

import java.util.Optional;

public interface VersionedRepository<K, E> extends Repository<K, E>
{
    VersionedEntity<K, E> init(K key);
    Optional<VersionedEntity<K, E>> exists(K key);
}
