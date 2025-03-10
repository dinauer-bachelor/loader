package extractor.core.database;

import com.arcadedb.remote.RemoteDatabase;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.sql.DataSource;
import java.rmi.Remote;
import java.sql.Connection;

public class DatabaseConnector {

    public static RemoteDatabase get() {
        String host = ConfigProvider.getConfig().getValue("arcadedb.host", String.class);
        Integer port = ConfigProvider.getConfig().getValue("arcadedb.port", Integer.class);
        String user = ConfigProvider.getConfig().getValue("arcadedb.user", String.class);
        String password = ConfigProvider.getConfig().getValue("arcadedb.password", String.class);
        String database = ConfigProvider.getConfig().getValue("arcadedb.database", String.class);
        return new RemoteDatabase(host, port, database, user, password);
    }

}
