package loader.persistance.repo;

public interface VersionedRepository<K, E> extends Repository<K, E>
{
    K init(K key);
}
