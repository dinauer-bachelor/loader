package loader.persistence.repo;

import com.arcadedb.graph.Vertex;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.DatabaseUtils;
import loader.persistance.Issue;
import loader.persistance.Project;
import loader.persistance.VersionedEntity;
import loader.persistance.repo.IssueRepo;
import loader.persistance.repo.ProjectRepo;
import org.graalvm.nativebridge.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class IssueRepoTest
{
    @Inject
    DatabaseUtils databaseUtils;

    @Inject
    ProjectRepo projectRepo;

    @Inject
    IssueRepo issueRepo;

    @Test
    void initIssue()
    {
        databaseUtils.setup();

        // Given
        String issueId = "KAH-438";
        String projectId = "KAH";

        // When
        issueRepo.init(issueId, "KAH");

        // Then
        assertTrue(projectRepo.exists(projectId).isPresent());
        assertTrue(issueRepo.exists(issueId).isPresent());
    }

    @Test
    void persistAndFindIssue()
    {
        databaseUtils.setup();

        // Given
        Issue issue = new Issue().setKey("KEH-233").setProjectKey("KEH").setSummary("Problem at step 3").setDescription("A problem occurred during manufacturing");

        // When
        issueRepo.persist(issue);

        // Then
        Optional<Vertex> optionalProject = projectRepo.exists("KEH");
        Optional<Issue> optionalIssue = issueRepo.findById("KEH-233");
        assertTrue(optionalProject.isPresent());
        assertTrue(optionalIssue.isPresent());
        assertEquals("KEH-233", optionalIssue.get().getKey());
        assertEquals("Problem at step 3", optionalIssue.get().getSummary());
        assertEquals("A problem occurred during manufacturing", optionalIssue.get().getDescription());
    }
}
