package loader.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.DatabaseUtils;
import loader.persistance.Comment;
import loader.persistance.Issue;
import loader.persistance.Project;
import loader.persistance.repo.CommentRepo;
import loader.persistance.repo.Database;
import loader.persistance.repo.IssueRepo;
import loader.persistance.repo.ProjectRepo;
import loader.service.CommentService;
import loader.service.IssueService;
import loader.service.ProjectService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@QuarkusTest
class GraphBuilder
{
    private static final String BASE_PATH = "C:\\Users\\andre\\Documents\\dev\\bachelorarbeit\\loader\\src\\main\\resources\\data";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Inject
    DatabaseUtils databaseUtils;

    @Inject
    ProjectService projectService;

    @Inject
    IssueService issueService;

    @Inject
    CommentService commentService;

    @Test
    @Disabled
    void build() throws IOException
    {
        databaseUtils.setup();

        List<Project> projects = OBJECT_MAPPER.readValue(Files.readString(Path.of(BASE_PATH, "projects.json")), new TypeReference<List<Project>>(){});
        for(Project project : projects)
        {
            projectService.handle(project);
        }

        List<Issue> issues = OBJECT_MAPPER.readValue(Files.readString(Path.of(BASE_PATH, "issues.json")), new TypeReference<List<Issue>>(){});
        for(Issue issue : issues)
        {
            issueService.handle(issue);
        }

        List<Comment> comments = OBJECT_MAPPER.readValue(Files.readString(Path.of(BASE_PATH, "comments.json")), new TypeReference<List<Comment>>(){});
        for(Comment comment : comments)
        {
            commentService.handle(comment);
        }
    }
}
