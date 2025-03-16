package loader;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.graalvm.nativebridge.In;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class Playground
{
    @Inject
    DatabaseUtils databaseUtils;

    @Test
    void playground()
    {
        databaseUtils.reset();
    }
}
