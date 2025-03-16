package loader.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import loader.persistance.Project;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

@ApplicationScoped
public class CommentConsumer
{
    @Incoming("comments")
    public void consume(Project project) throws JsonProcessingException
    {
        throw new NotImplementedYet();
    }
}
