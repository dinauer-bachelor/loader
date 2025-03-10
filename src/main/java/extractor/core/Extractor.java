package extractor.core;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class Extractor {

    private final ProjectExtractor projectExtractor;

    @Inject
    public Extractor(extractor.core.ProjectExtractor projectExtractor) {
        this.projectExtractor = projectExtractor;

    }

    @PostConstruct
    public void run() throws ExecutionException, InterruptedException, IOException {
        ZonedDateTime from = null; //Pull timestamp from last execution.
        ZonedDateTime now = ZonedDateTime.now(); // Pull last run's datetime here to extract all changes from source system.
        Map<String, List<String>> projectConfigs = new ObjectMapper().readValue(new File("src/main/resources/config.json"), new TypeReference<Map<String, List<String>>>() {});
        projectExtractor.run(projectConfigs);
    }

}