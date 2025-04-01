package loader.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Issue;
import loader.persistance.Project;
import loader.service.IssueService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class IssueConsumer
{
    @Inject
    IssueService issueService;

    @Incoming("issues")
    public void consume(Issue issue)
    {
        System.out.println("recieved comment " + issue.getKey());
        issueService.handle(issue);
    }
}
