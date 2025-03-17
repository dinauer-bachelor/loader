package loader;

import com.arcadedb.database.RID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import loader.persistance.Project;
import loader.persistance.VersionedEntity;
import loader.persistance.repo.EdgeRepository;
import loader.persistance.repo.ProjectRepo;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Path("/dev")
public class Dev
{
    @Inject
    ProjectRepo projectRepo;

    @Inject
    DatabaseUtils databaseUtils;

    @POST
    @Consumes
    @Produces
    public void post(Project project)
    {
        persistProject(project);
    }

    @POST
    @Path("/setup")
    @Consumes
    @Produces
    public void reset()
    {
        databaseUtils.setup();
    }

    @GET
    @Path("/projects/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Project getById() throws JsonProcessingException {
        return projectRepo.findById("LEK").get();
    }

    @POST
    @Path("/edge")
    @Produces
    @Consumes
    public void findOutgoingEdges(String outgoing) {
    }

    private void init()
    {
    }

    private void persistProject(Project project)
    {
        projectRepo.persist(project);
    }
}
