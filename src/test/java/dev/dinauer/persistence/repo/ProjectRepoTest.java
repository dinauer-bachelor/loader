package dev.dinauer.persistence.repo;

import com.arcadedb.graph.Vertex;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import dev.dinauer.DatabaseUtils;
import dev.dinauer.persistence.entity.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@QuarkusTest
public class ProjectRepoTest
{
    @Inject
    DatabaseUtils databaseUtils;

    @Inject
    ProjectRepo projectRepo;

    @Test
    void initProject()
    {
        databaseUtils.setup();

        // Given
        String projectId = "KAH";

        // When
        Vertex versionedProject = projectRepo.init(projectId);
        projectRepo.init(projectId);

        // Then
        Assertions.assertEquals(0, versionedProject.getInteger("count"));
        Assertions.assertEquals("KAH", versionedProject.getString("key"));
        Assertions.assertNotNull(versionedProject.getIdentity());
    }

    @Test
    void persistAndFindProject()
    {
        databaseUtils.setup();

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
