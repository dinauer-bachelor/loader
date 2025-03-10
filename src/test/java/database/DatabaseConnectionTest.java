package database;

import extractor.core.database.DatabaseConnector;
import org.junit.jupiter.api.Test;

public class DatabaseConnectionTest {

    @Test
    void test() {
        DatabaseConnector.get();
    }

}
