package extractor.repo.source;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.domain.Issue;

import extractor.core.jql.JqlBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IssueSourceRepo {

    public IssueSourceRepo() {
    }

    public Iterable<Issue> findByJql(String jql) {
        try {
            return SourceClient.build().getSearchClient().searchJql(jql).get().getIssues();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public Iterable<Issue> findByProjectKeyAndCreatedSince(String projectKey, ZonedDateTime createdSince) throws ExecutionException, InterruptedException {
        return SourceClient.build().getSearchClient().searchJql(JqlBuilder.build(projectKey, null, List.of())).get().getIssues();
    }


}