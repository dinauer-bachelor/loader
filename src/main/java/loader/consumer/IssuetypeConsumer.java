package loader.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import loader.persistance.Project;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class IssuetypeConsumer
{
    @Incoming("issuetypes")
    public void consume(Project project) throws JsonProcessingException
    {
        System.out.println(new ObjectMapper().writeValueAsString(project));
    }
}
