package loader.persistence.repo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.persistance.Project;
import loader.persistance.VersionedEntity;
import loader.persistance.repo.ProjectRepo;
import loader.service.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@QuarkusTest
public class ProjectRepoTest
{
    @Inject
    ProjectRepo projectRepo;

    @Test
    void initProject()
    {
        // Given
        String projectId = "KAH";

        // When
        VersionedEntity<String, Project> versionedProject = projectRepo.init(projectId);

        // Then
        Assertions.assertEquals(0, versionedProject.getCount());
        Assertions.assertEquals("KAH", versionedProject.getId());
        Assertions.assertNotNull(versionedProject.getRid());
    }

    @Test
    void persistAndFindProject()
    {
        // Given
        Project project1 = new Project().setName("Menuflow").setKey("KAH").setDescription("This project is for menuflow development.").setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC));
        Project project2 = new Project().setName("Menuflow #2").setKey("KAH").setDescription("This project is for menuflow development.").setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC));

        // When
        projectRepo.persist(project1);
        projectRepo.persist(project2);

        // Then
        Optional<Project> optionalProject = projectRepo.findById("KAH");
        Assertions.assertTrue(optionalProject.isPresent());
        Assertions.assertEquals("Menuflow #2", optionalProject.get().getName());
        Assertions.assertEquals("KAH", optionalProject.get().getKey());
    }
}
