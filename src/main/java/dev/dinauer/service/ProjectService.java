package dev.dinauer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.dinauer.persistence.entity.Project;
import dev.dinauer.persistence.repo.ProjectRepo;

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
