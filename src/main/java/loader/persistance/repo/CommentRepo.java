package loader.persistance.repo;

import com.arcadedb.database.RID;
import loader.persistance.Comment;
import loader.persistance.VersionedEntity;
import org.apache.kafka.common.protocol.types.Field;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.Optional;

public class CommentRepo implements VersionedRepository<String, Comment>
{
    @Override
    public VersionedEntity<String, Comment> init(String key)
    {
        throw new NotImplementedYet();
    }

    @Override
    public Optional<VersionedEntity<String, Comment>> exists(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Comment> findById(String key)
    {
        throw new NotImplementedYet();
    }

    @Override
    public List<Comment> findAll()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Comment persist(Comment entity)
    {
        throw new NotImplementedYet();
    }

    @Override
    public Comment delete(Comment entity)
    {
        throw new NotImplementedYet();
    }
}
