package loader;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.persistance.Project;
import loader.persistance.VersionedEntity;
import loader.persistance.repo.ProjectRepo;
import loader.service.ProjectService;
import org.graalvm.nativebridge.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PersistProjectTest
{
    @Inject
    ProjectService projectService;

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
    void persistProject()
    {
        Project project = new Project().setName("Menuflow").setKey("KAH").setDescription("This project is for menuflow development.");
        projectService.handle(project);

    }
}
