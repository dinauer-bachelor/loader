package loader.persistance.repo;

import com.arcadedb.database.RID;
import com.arcadedb.database.Record;
import com.arcadedb.graph.Edge;
import com.arcadedb.graph.Vertex;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Comment;
import loader.persistance.Issue;
import loader.persistance.Project;
import loader.persistance.VersionedEntity;
import org.apache.kafka.common.protocol.types.Field;
import org.jboss.resteasy.reactive.common.NotImplementedYet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.awt.color.CMMException;
import java.util.*;

@ApplicationScoped
public class CommentRepo implements VersionedRepository<String, Comment>
{
    private static final Logger LOG = LoggerFactory.getLogger(CommentRepo.class);

    private static final String HAS_STATE = "has_comment_state";

    private static final String LATEST_STATE = "latest_comment_state";

    @Inject
    Database database;

    @Inject
    IssueRepo issueRepo;

    public Vertex init(String commentId, String issueKey, String projectKey)
    {
        Optional<Vertex> exists = exists(commentId);
        if(exists.isEmpty())
        {
            try(RemoteDatabase remoteDB = database.get())
            {
                Vertex comment = remoteDB.newVertex("comment_id").set("id", commentId).save();
                Vertex issue = remoteDB.lookupByRID(issueRepo.init(issueKey, projectKey).getIdentity()).asVertex(); // Initialized and retrieves the corresponding issue for creating its edge
                issue.newEdge("belongs_to_issue", comment, false).save();
                LOG.info("Initialized comment '{}'", commentId);
                return comment;
            }
        } else
        {
            return exists.get();
        }
    }

    @Override
    public Optional<Vertex> exists(String id) {
        String sql = "SELECT FROM comment_id WHERE id = :id;";
        try(RemoteDatabase remoteDB = database.get()) {
            ResultSet rs = remoteDB.command("sql", sql, Map.ofEntries(new AbstractMap.SimpleEntry<>("id", id)));
            if(rs.hasNext())
            {
                return rs.next().getVertex();
            } else
            {
                return Optional.empty();
            }
        }
    }

    @Override
    public List<VersionedEntity<String, Comment>> findAllVersions()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Optional<Comment> findById(String id)
    {
        Optional<Vertex> x = exists(id);
        if(x.isPresent()) {
            try(RemoteDatabase remoteDB = database.get())
            {
                Vertex commentId = x.get();
                Iterator<Edge> edges = remoteDB.lookupByRID(commentId.getIdentity()).asVertex().getEdges(Vertex.DIRECTION.OUT, LATEST_STATE).iterator();
                if(edges.hasNext())
                {
                    Edge edge = edges.next();
                    Vertex comment = edge.getInVertex();
                    return Optional.of(vertexToComment(comment));
                }
            }
        }
        return Optional.empty();
    }



    @Override
    public List<Comment> findAll()
    {
        throw new NotImplementedYet();
    }

    @Override
    public Comment persist(Comment entity)
    {
        Vertex versionedComment = init(entity.getId(), entity.getIssueKey(), entity.getProjectKey());
        try(RemoteDatabase remoteDB = database.get())
        {
            remoteDB.begin();
            Vertex issueState = remoteDB.newVertex("comment")
                .set("id", entity.getId())
                .set("issue_key", entity.getIssueKey())
                .set("project_key", entity.getProjectKey())
                .set("text", entity.getText())
                .set("author", entity.getAuthor())
                .save();
            Vertex base = remoteDB.lookupByRID(versionedComment.getIdentity()).asVertex();
            base.newEdge(HAS_STATE, issueState, false);
            reconnectLatestState(base, issueState);
            remoteDB.commit();
            LOG.info("Persisted state of comment '{}'", entity.getId());
        }
        return entity;
    }

    @Override
    public Comment delete(Comment entity)
    {
        throw new NotImplementedYet();
    }

    private Comment vertexToComment(Vertex vertex)
    {
        Comment comment = new Comment();
        comment.setId(vertex.getString("id"));
        comment.setIssueKey(vertex.getString("issue_key"));
        comment.setProjectKey(vertex.getString("project_key"));
        comment.setText(vertex.getString("text"));
        comment.setAuthor(vertex.getString("author"));
        return comment;
    }

    private void reconnectLatestState(Vertex base, Vertex latestState)
    {
        base.getEdges(Vertex.DIRECTION.OUT, LATEST_STATE).forEach(Record::delete);
        base.newEdge(LATEST_STATE, latestState, true);
    }
}
