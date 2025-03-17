package loader.persistance.repo;

import com.arcadedb.database.RID;
import com.arcadedb.graph.Vertex;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Comment;
import loader.persistance.VersionedEntity;
import org.apache.kafka.common.protocol.types.Field;
import org.jboss.resteasy.reactive.common.NotImplementedYet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CommentRepo implements VersionedRepository<String, Comment>
{
    private static final Logger LOG = LoggerFactory.getLogger(CommentRepo.class);

    @Inject
    Database database;

    @Inject
    IssueRepo issueRepo;

    public Vertex init(String commentId, String issueKey, String projectKey)
    {
        Optional<Vertex> exists = exists(issueKey);
        if(exists.isEmpty())
        {
            try(RemoteDatabase remoteDB = database.get())
            {
                remoteDB.begin();
                Vertex comment = remoteDB.newVertex("comment_id").set("id", commentId).save();
                Vertex issue = remoteDB.lookupByRID(issueRepo.init(issueKey, projectKey).getIdentity()).asVertex();
                issue.newEdge("belongs_to_issue", comment, false).save();
                remoteDB.commit();
                LOG.info("Initialized comment '{}'", commentId);
                return issue;
            }
        } else
        {
            return exists.get();
        }
    }

    @Override
    public Optional<Vertex> exists(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Comment> findById(String key)
    {
        throw new NotImplementedYet();
    }

    @Override
    public List<Comment> findAll()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Comment persist(Comment entity)
    {
        throw new NotImplementedYet();
    }

    @Override
    public Comment delete(Comment entity)
    {
        throw new NotImplementedYet();
    }
}
