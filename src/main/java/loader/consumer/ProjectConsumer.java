package loader.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Project;
import loader.service.ProjectService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ProjectConsumer
{
    @Inject
    ProjectService projectService;

    @Incoming("projects")
    public void consume(Project project) throws JsonProcessingException
    {
        projectService.handle(project);
    }
}
