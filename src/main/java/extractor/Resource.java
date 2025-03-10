package extractor;

import extractor.core.Extractor;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Path("/run")
public class Resource {

    private final Extractor extractor;

    @Inject
    public Resource(Extractor extractor) {
        this.extractor = extractor;
    }

    @POST
    public void run() throws ExecutionException, InterruptedException, IOException {
        extractor.run();
    }

}