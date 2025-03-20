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
            .setDescription("Part LO8344 was damaged and could not be mounted.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("KAH-345")
            .setProjectKey("KAH")
            .setSummary("Problem with mounting part LO8344 to machine.")
            .setDescription("Part LO8344 was damaged and could not be mounted. EDIT: We found out that our forklift operator did this.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).minusDays(3)));
        issueRepo.persist(new Issue()
            .setKey("KAH-346")
            .setProjectKey("KAH")
            .setSummary("Problem with mounting part UZ5342.")
            .setDescription("The part was delivered too late and was returned. This needs to be improved.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("KAH-347")
            .setProjectKey("KAH")
            .setSummary("Engine did not start at end of production of model CF3GT.")
            .setDescription("The vehicle could not be started due to an error with its engine. Most likely wrong engine mounted.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-67")
            .setProjectKey("PUH")
            .setSummary("Racoon disturbs production line.")
            .setDescription("production was temporarily halted when a raccoon entered the assembly line. The animal managed to navigate through the open loading dock and caused a brief disruption by scavenging a tool cart.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-69")
            .setProjectKey("PUH")
            .setSummary("Marco brought his cat with him to work.")
            .setDescription("Marco was sent home today and we needed to stop our production due to him bringing his cat to work. This was expensive for us.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).minusDays(12)));
        issueRepo.persist(new Issue()
            .setKey("PUH-69")
            .setProjectKey("PUH")
            .setSummary("Marco brought his cat with him to work.")
            .setDescription("Marco was sent home today and we needed to stop our production due to him bringing his cat to work. This was expensive for us. We now decided to inform all workers that it is not allowed to bring pets to work.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).minusDays(5)));
        issueRepo.persist(new Issue()
            .setKey("PUH-69")
            .setProjectKey("PUH")
            .setSummary("Marco brought his cat with him to work.")
            .setDescription("Marco was sent home today and we needed to stop our production due to him bringing his cat to work. This was expensive for us. We now decided to inform all workers that it is not allowed to bring pets to work. EDIT: Everyone who brings pets to work will be sent home and will be charged a fine of 25 euros.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-70")
            .setProjectKey("PUH")
            .setSummary("Problem with Part 6565.")
            .setDescription("Part 6565 collided with another part when mounting it to our latest model the CD3. This needs to be resolved. Our head of productions knows how to resolve this issue. Please talk to him.")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-167")
            .setProjectKey("PUH")
            .setSummary("Problem with welding at station 3.")
            .setDescription("When we tried to weld a part onto our latest model CD3. We encountered the problem that the welding was not strong enough to hold the part.")
            .setStatus("RESOLVED")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-169")
            .setProjectKey("PUH")
            .setSummary("Welding issues at station 3.")
            .setDescription("Welding a part in production was not possible due to weak welding technique. The car was removed from production line.")
            .setStatus("RESOLVED")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-168")
            .setProjectKey("PUH")
            .setSummary("Food poisoning at CD3 production.")
            .setDescription("A coworker brought a big party prezel to work and almost 20 people at from it resulting in 18 of them being sick. Some for multiple days. It was later found out that they caught norovirus.")
            .setStatus("RESOLVED")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));
        issueRepo.persist(new Issue()
            .setKey("PUH-170")
            .setProjectKey("PUH")
            .setSummary("Tires were delayed for production line of CD3 at station 4.")
            .setDescription("Our employees tried to mount tires to a vehicle when there were no tires delivered. They were delayed for 2 hours and production was halted. This needs improvement.")
            .setStatus("OPEN")
            .setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC)));

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
