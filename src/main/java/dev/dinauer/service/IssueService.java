package dev.dinauer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.dinauer.persistence.entity.Issue;
import dev.dinauer.persistence.repo.IssueRepo;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@ApplicationScoped
public class IssueService
{
    @Inject
    IssueRepo issueRepo;

    public void handle(Issue issue)
    {
        issue.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        issueRepo.persist(issue);
    }
}
