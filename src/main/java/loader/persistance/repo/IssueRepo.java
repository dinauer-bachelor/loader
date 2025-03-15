package loader.persistance.repo;

import loader.persistance.Issue;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.Optional;

public class IssueRepo implements VersionedRepository<String, Issue>
{
    @Override
    public String init(String key)
    {
        throw new NotImplementedYet();
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
