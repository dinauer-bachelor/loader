package loader.persistance.repo;

import loader.persistance.Issuetype;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.Optional;

public class IssuetypeRepo implements Repository<String, Issuetype>
{
    @Override
    public Optional<Issuetype> findById(String key)
    {
        throw new NotImplementedYet();
    }

    @Override
    public List<Issuetype> findAll()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Issuetype persist(Issuetype entity)
    {
        throw new NotImplementedYet();
    }

    @Override
    public Issuetype delete(Issuetype entity)
    {
        throw new NotImplementedYet();
    }
}
