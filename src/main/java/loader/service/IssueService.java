package loader.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Issue;
import loader.persistance.repo.IssueRepo;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@ApplicationScoped
public class IssueService
{
    @Inject
    IssueRepo issueRepo;

    public void handle(Issue issue)
    {
        System.out.println(issue.getKey() + issue.getProjectKey());
        issue.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        issueRepo.persist(issue);
    }
}
