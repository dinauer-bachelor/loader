package loader.persistence;

import com.arcadedb.query.sql.executor.BreakStep;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.DatabaseUtils;
import loader.persistance.Issue;
import loader.persistance.Project;
import loader.persistance.repo.CommentRepo;
import loader.persistance.repo.Database;
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

    @Inject
    Database database;

    @Test
    void build()
    {
        databaseUtils.setup();

        // Comments
        commentRepo.init("20001", "KAH-345", "KAH");
    }

    @Test
    void testSimilarity()
    {
        databaseUtils.setup();

        // given
        Issue issue1 = new Issue().setKey("KEH-233").setProjectKey("KEH").setSummary("Implement OAuth2 Login for the User Portal").setDescription("Integrate OAuth2 authentication into the user portal to enable secure login using Google and Microsoft accounts. The feature should follow existing login patterns and store essential user profile information in our user management system. Ensure token validation, refresh logic, and error handling are properly covered. Update documentation accordingly.").setCreatedAt(ZonedDateTime.now().minusDays(10));
        Issue issue2 = new Issue().setKey("KEH-234").setProjectKey("KEH").setSummary("Fix UI Overlap Issue on Mobile Devices").setDescription(" On mobile screens (≤ 768px), the profile dropdown overlaps with the navigation bar, blocking other elements. This issue has been reported on both iOS and Android browsers. Investigate the CSS rules, especially z-index and flex container behavior. Deliver a fix that maintains accessibility and responsive design standards.").setCreatedAt(ZonedDateTime.now().minusDays(5));
        Issue issue3 = new Issue().setKey("KEH-235").setProjectKey("KEH").setSummary("Add OAuth2 Support to Admin Dashboard").setDescription(" Extend the OAuth2 login feature to the admin dashboard, ensuring admins can authenticate using corporate Microsoft accounts. The implementation should reuse the existing OAuth2 module developed for the user portal, with adjustments for admin roles and scopes. Conduct thorough testing to avoid session conflicts between user and admin logins.").setCreatedAt(ZonedDateTime.now().minusDays(2));
        Issue issue4 = new Issue().setKey("KEH-236").setProjectKey("KEH").setSummary("Implement OAuth2 Login for the Mobile App").setDescription(" Introduce OAuth2 authentication in the mobile application, allowing users to log in using their Google and Microsoft accounts. This should mirror the behavior and flow of the user portal implementation, including token handling, user profile sync, and error messaging. Leverage shared authentication logic where possible, ensuring compatibility with the mobile platform’s lifecycle and security standards. Document platform-specific behaviors and edge cases.").setCreatedAt(ZonedDateTime.now());
        Issue issue5 = new Issue().setKey("KEH-235").setProjectKey("KEH").setSummary("Implement OAuth2 Login for the Mobile App").setDescription(" Introduce OAuth2 authentication in the mobile application, allowing users to log in using their Google and Microsoft accounts. This should mirror the behavior and flow of the user portal implementation, including token handling, user profile sync, and error messaging. Leverage shared authentication logic where possible, ensuring compatibility with the mobile platform’s lifecycle and security standards. Ignore platform-specific behaviors and edge cases.").setCreatedAt(ZonedDateTime.now());

        // when
        issueRepo.persist(issue1);
        issueRepo.persist(issue2);
        issueRepo.persist(issue3);
        issueRepo.persist(issue4);
        issueRepo.persist(issue5);

        // then
        System.out.println("Lets see");
    }

    @Test
    void retrieve()
    {
        String sql = "SELECT FROM (TRAVERSE out() FROM (TRAVERSE out() FROM project_id) MAXDEPTH 5)";
        try(RemoteDatabase remoteDB = database.get())
        {
            ResultSet rs = remoteDB.command(Database.SQL, sql);
            System.out.println(rs);
            while(rs.hasNext())
            {
                Result result = rs.next();
            }
        }
    }
}
