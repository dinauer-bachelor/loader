package dev.dinauer.consumer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.dinauer.persistence.entity.Issue;
import dev.dinauer.service.IssueService;
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
