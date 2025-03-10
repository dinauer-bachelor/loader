package extractor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import extractor.core.jql.JqlBuilder;

import java.util.List;
import java.util.Map;

public class JqlBuilderTest {

    @Test
    public void test() {
        Assertions.assertEquals("project = 'ECW' AND (issuetype = 'Task' OR issuetype = 'Bug') AND (created > 0 OR updated > 0)", JqlBuilder.build("ECW", List.of("Task", "Bug"), List.of("created > 0 OR updated > 0")));
    }

}