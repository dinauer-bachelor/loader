package dev.dinauer.persistence.repo;

import com.arcadedb.remote.RemoteDatabase;
import com.arcadedb.remote.RemoteServer;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
public class Database
{
    public static final String SQL_SCRIPT = "sqlscript";
    public static final String SQL = "sqlscript";

    @ConfigProperty(name = "arcadedb.host")
    String host;

    @ConfigProperty(name = "arcadedb.port")
    Integer port;

    @ConfigProperty(name = "arcadedb.database")
    String database;

    @ConfigProperty(name = "arcadedb.user")
    String user;

    @ConfigProperty(name = "arcadedb.password")
    String password;

    public RemoteDatabase get()
    {
        RemoteServer remoteServer = getServer();
        List<String> databases = remoteServer.databases();
        if (!databases.contains(database))
        {
            remoteServer.create(database);
        }
        return new RemoteDatabase(host, port, database, user, password);
    }

    private RemoteServer getServer()
    {
        return new RemoteServer(host, port, user, password);
    }
}
