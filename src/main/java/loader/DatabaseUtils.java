package loader;

import com.arcadedb.remote.RemoteDatabase;
import com.arcadedb.remote.RemoteSchema;
import com.arcadedb.schema.DocumentType;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.repo.Database;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Startup
@ApplicationScoped
public class DatabaseUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtils.class);

    private static final String DROP_TYPE = "DROP TYPE %s IF EXISTS UNSAFE;";

    @ConfigProperty(name = "arcadedb.clear")
    Boolean devServicesEnabled;

    @Inject
    Database database;

    @PostConstruct
    public void onStartup()
    {
        if(devServicesEnabled)
        {
            setup();
        }
    }

    public void reset()
    {
        try(RemoteDatabase remoteDB = database.get())
        {
            RemoteSchema schema = database.get().getSchema();
            for(DocumentType type : schema.getTypes())
            {
                remoteDB.command(Database.SQL_SCRIPT, String.format(DROP_TYPE, type.getName()));
            }
            LOG.info("Dropped all types from database");
        }
    }

    public void setup() {
        reset();
        try(RemoteDatabase remoteDB = database.get())
        {
            String sql = Resources.toString(Resources.getResource("import.sql"), Charsets.UTF_8);
            remoteDB.command(Database.SQL_SCRIPT, sql);
            LOG.info("Database setup successful");
        } catch (IOException e)
        {
            LOG.error("Cannot read import.sql from resources");
        }
    }
}
