package dev.dinauer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.dinauer.persistence.entity.Project;
import dev.dinauer.service.ProjectService;
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
