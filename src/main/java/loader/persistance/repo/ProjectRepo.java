package loader.persistance.repo;

import jakarta.enterprise.context.ApplicationScoped;
import loader.persistance.Project;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProjectRepo implements VersionedRepository<String, Project>
{
    @Override
    public String init(String key) {
        return null;
    }

    @Override
    public Optional<Project> findById(String id)
    {
        if(id.equals("KAN"))
        {
            return Optional.of(new Project().setKey("KAN").setName("MenuFlow").setDescription(""));
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        throw new NotImplementedYet();
    }

    @Override
    public Project persist(Project project)
    {
        throw new NotImplementedYet();
    }

    @Override
    public Project delete(Project entity) {
        throw new NotImplementedYet();
    }
}
