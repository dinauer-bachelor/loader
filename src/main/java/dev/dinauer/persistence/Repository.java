package dev.dinauer.persistence;

import java.util.List;
import java.util.Optional;

public interface Repository<K, E>
{
    Optional<E> findById(K key);

    List<E> findAll();

    E persist(E entity);

    E delete(E entity);
}
