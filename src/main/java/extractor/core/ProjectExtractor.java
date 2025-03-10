package extractor.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Project;
import extractor.persistance.target.ProjectTarget;
import extractor.repo.source.SourceClient;
import extractor.repo.target.ProjectTargetRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ProjectExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectExtractor.class);

    @Inject
    IssueExtractor issueExtractor;

    @Inject
    ProjectTargetRepo projectTargetRepo;

    public void run(Map<String, List<String>> projectConfigs) throws IOException {
        for(Map.Entry<String, List<String>> projectConfig : projectConfigs.entrySet()) {
            run(projectConfig.getKey());
            issueExtractor.run(projectConfig.getKey(), projectConfig.getValue());
        }
    }

    public void run(String projectKey) {
        try(JiraRestClient client = SourceClient.build()) {
            ProjectTarget extractedProject = ProjectTarget.fromProject(client.getProjectClient().getProject(projectKey).get());
            Optional<ProjectTarget> existingProjectOptional = projectTargetRepo.findByKey(projectKey);
            if(existingProjectOptional.isPresent()) {
                ProjectTarget existingProject = existingProjectOptional.get();
                if(!existingProject.equals(extractedProject)) {
                    LOG.info("Project {} has changed and will be loaded.", projectKey);
                    projectTargetRepo.persist(extractedProject);
                } else {
                    LOG.info("Project {} has not changed.", projectKey);
                }
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}