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
class Playground
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

        // Comments
        commentRepo.init("20001", "KAH-345", "KAH");
    }
}
