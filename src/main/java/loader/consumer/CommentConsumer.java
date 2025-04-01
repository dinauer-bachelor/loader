package loader.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Comment;
import loader.persistance.Project;
import loader.service.CommentService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

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
