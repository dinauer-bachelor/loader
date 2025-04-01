package loader.persistence.repo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import loader.persistance.Issuetype;
import loader.persistance.repo.IssuetypeRepo;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class IssuetypeRepoTest
{
    @Inject
    IssuetypeRepo issuetypeRepo;

    @Test
    void testPersist()
    {
        issuetypeRepo.persist(new Issuetype());
    }
}
