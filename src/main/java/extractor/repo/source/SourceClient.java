package extractor.repo.source;

import java.net.URI;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public interface SourceClient {

    static JiraRestClient build() {
        Config config = ConfigProvider.getConfig();
        String baseUri = config.getValue("jira.url", String.class);
        String user = config.getValue("jira.user", String.class);
        String password = config.getValue("jira.password", String.class);
        return new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(URI.create(baseUri), user, password);
    }

}
