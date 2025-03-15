package loader.persistance.repo;

import loader.persistance.Comment;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.Optional;

public class CommentRepo implements VersionedRepository<String, Comment>
{
    @Override
    public String init(String key)
    {
        throw new NotImplementedYet();
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
