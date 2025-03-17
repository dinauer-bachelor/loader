package loader.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.DatabaseUtils;
import loader.persistance.Issue;
import loader.persistance.Project;
import loader.persistance.repo.CommentRepo;
import loader.persistance.repo.IssueRepo;
import loader.persistance.repo.ProjectRepo;
import org.graalvm.nativebridge.In;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

//SELECT FROM (TRAVERSE out('belongs_to_issue') FROM (TRAVERSE out('belongs_to_project', 'has_project_state') FROM project_id) MAXDEPTH 5 )

@QuarkusTest
class SampleGraphBuilder
{
    @Inject
    DatabaseUtils databaseUtils;

    @Inject
    ProjectRepo projectRepo;

    @Inject
    IssueRepo issueRepo;

    @Inject
    CommentRepo commentRepo;

    @Test
    void build()
    {
        databaseUtils.setup();

        // Projects
        projectRepo.persist(new Project()
            .setKey("KAH")
            .setName("Production CD3")
            .setDescription("This project handles errors during production of our latest model CD3.")
            .setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC).minusDays(1)));
        projectRepo.persist(new Project()
            .setKey("KAH")
            .setName("Production CD3")
            .setDescription("This project handles errors during production of our latest model CD3 and also its derivatives CD3-X and CF3-E.")
            .setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        projectRepo.persist(new Project()
            .setKey("PUH")
            .setName("Engine Assembly Defect Monitoring")
            .setDescription("This project monitors and addresses defects during engine assembly in the production of the new model 'Vortex' sports car.")
            .setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        projectRepo.persist(new Project()
            .setKey("KOE")
            .setName("Chassis Alignment Quality Assurance")
            .setDescription("This project ensures the precision of chassis alignment during the assembly of the 'Raptor X' electric SUV, aiming to eliminate misalignment errors.")
            .setInsertedAt(ZonedDateTime.now(ZoneOffset.UTC)));

        // Issues
        issueRepo.persist(new Issue()
            .setKey("KAH-345")
            .setProjectKey("KAH")
            .setSummary("Problem with mounting part LO8344 to machine.")
            .setDescription("Part LO8344 was damaged and could not be mounted."));
        issueRepo.persist(new Issue()
            .setKey("KAH-345")
            .setProjectKey("KAH")
            .setSummary("Problem with mounting part LO8344 to machine.")
            .setDescription("Part LO8344 was damaged and could not be mounted. EDIT: We found out that our forklift operator did this."));
        issueRepo.persist(new Issue()
            .setKey("KAH-346")
            .setProjectKey("KAH")
            .setSummary("Problem with mounting part UZ5342.")
            .setDescription("The part was delivered too late and was returned. This needs to be improved."));
        issueRepo.persist(new Issue()
            .setKey("KAH-347")
            .setProjectKey("KAH")
            .setSummary("Engine did not start at end of production of model CF3GT.")
            .setDescription("The vehicle could not be started due to an error with its engine. Most likely wrong engine mounted."));
        issueRepo.init("PUH-67", "PUH");
        issueRepo.init("PUH-69", "PUH");
        issueRepo.init("KOE-167", "KOE");
        issueRepo.init("KOE-169", "KOE");
        issueRepo.init("KOE-168", "KOE");
        issueRepo.init("KOE-170", "KOE");

        // Comments
        commentRepo.init("20001", "KAH-345", "KAH");
        commentRepo.init("20002", "KAH-346", "KAH");
        commentRepo.init("20003", "KAH-347", "KAH");
        commentRepo.init("20004", "PUH-67", "PUH");
        commentRepo.init("20005", "PUH-69", "PUH");
        commentRepo.init("20006", "KOE-167", "KOE");
        commentRepo.init("20007", "KOE-169", "KOE");
        commentRepo.init("20008", "KOE-170", "KOE");
        commentRepo.init("20009", "KOE-168", "KOE");
        commentRepo.init("20010", "KOE-168", "KOE");
    }
}
