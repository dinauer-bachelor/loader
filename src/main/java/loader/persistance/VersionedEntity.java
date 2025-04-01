package loader.persistance;

import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

public class VersionedEntity<K, E> extends Entity
{
    private K id;
    private E latest;
    private int count;
    private List<E> versions;

    public K getId() {
        return id;
    }

    public VersionedEntity<K, E> setId(K id) {
        this.id = id;
        return this;
    }

    public E getLatest() {
        return latest;
    }

    public VersionedEntity<K, E> setLatest(E latest) {
        this.latest = latest;
        return this;
    }

    public int getCount() {
        return count;
    }

    public VersionedEntity<K, E> setCount(int count) {
        this.count = count;
        return this;
    }

    public List<E> getVersions() {
        throw new NotImplementedYet();
    }

    public VersionedEntity<K, E> setVersions(List<E> versions) {
        throw new NotImplementedYet();
    }
}
