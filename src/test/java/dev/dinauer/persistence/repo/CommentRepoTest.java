package dev.dinauer.persistence.repo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import dev.dinauer.DatabaseUtils;
import dev.dinauer.persistence.entity.Comment;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class CommentRepoTest
{
    @Inject
    DatabaseUtils databaseUtils;

    @Inject
    CommentRepo commentRepo;

    @Inject
    IssueRepo issueRepo;

    @Inject
    ProjectRepo projectRepo;

    @Test
    void initComment()
    {
        databaseUtils.setup();

        // Given
        String commentId = "2001";
        String issueKey = "KAH-344";
        String projectKey = "KAH";

        // When
        commentRepo.init(commentId, issueKey, projectKey);

        // Then
        assertTrue(projectRepo.exists("KAH").isPresent());
        assertTrue(issueRepo.exists("KAH-344").isPresent());
        assertTrue(commentRepo.exists("2001").isPresent());
    }

    @Test
    void persistComment()
    {
        databaseUtils.setup();

        // Given
        Comment comment = new Comment().setId("2001").setIssueKey("KAH-344").setProjectKey("KAH").setText("Issue was resolved with help of Chat GPT :)");

        // When
        commentRepo.persist(comment);

        // Then
        Optional<Comment> optionalComment = commentRepo.findById("2001");
        assertTrue(optionalComment.isPresent());
        assertEquals("Issue was resolved with help of Chat GPT :)", optionalComment.get().getText());
    }
}
