package dev.dinauer.persistence;

import com.arcadedb.graph.Vertex;
import dev.dinauer.persistence.Repository;
import dev.dinauer.persistence.VersionedEntity;

import java.util.List;
import java.util.Optional;

public interface VersionedRepository<K, E> extends Repository<K, E>
{
    Optional<Vertex> exists(K key);

    List<VersionedEntity<K, E>> findAllVersions();
}
