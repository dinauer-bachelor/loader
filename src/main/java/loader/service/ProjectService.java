package loader.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Project;
import loader.persistance.repo.ProjectRepo;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@ApplicationScoped
public class ProjectService
{
    @Inject
    ProjectRepo projectRepo;

    public void handle(Project project)
    {
        project.setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC));
        projectRepo.persist(project);
    }
}
