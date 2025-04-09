package dev.dinauer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.dinauer.persistence.entity.Comment;
import dev.dinauer.service.CommentService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class CommentConsumer
{
    @Inject
    CommentService commentService;

    @Incoming("comments")
    public void consume(Comment comment) throws JsonProcessingException
    {
        commentService.handle(comment);
    }
}
