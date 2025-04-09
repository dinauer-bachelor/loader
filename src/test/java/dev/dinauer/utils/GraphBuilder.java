package dev.dinauer.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dinauer.DatabaseUtils;
import dev.dinauer.persistence.entity.Comment;
import dev.dinauer.persistence.entity.Issue;
import dev.dinauer.persistence.entity.Project;
import dev.dinauer.service.CommentService;
import dev.dinauer.service.IssueService;
import dev.dinauer.service.ProjectService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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

    private static final TypeReference<List<Project>> projectList = new TypeReference<List<Project>>()
    {
    };

    private static final TypeReference<List<Issue>> issueList = new TypeReference<List<Issue>>()
    {
    };

    private static final TypeReference<List<Comment>> commentList = new TypeReference<List<Comment>>()
    {
    };

    @Inject
    DatabaseUtils databaseUtils;

    @Inject
    ProjectService projectService;

    @Inject
    IssueService issueService;

    @Inject
    CommentService commentService;

    @Test
    void build() throws IOException
    {
        databaseUtils.setup();

        List<Project> projects = OBJECT_MAPPER.readValue(Files.readString(Path.of(BASE_PATH, "projects.json")), projectList);
        for (Project project : projects)
        {
            projectService.handle(project);
        }

        List<Issue> issues = OBJECT_MAPPER.readValue(Files.readString(Path.of(BASE_PATH, "issues.json")), issueList);
        for (Issue issue : issues)
        {
            issueService.handle(issue);
        }

        List<Comment> comments = OBJECT_MAPPER.readValue(Files.readString(Path.of(BASE_PATH, "comments.json")), commentList);
        for (Comment comment : comments)
        {
            commentService.handle(comment);
        }
    }
}
