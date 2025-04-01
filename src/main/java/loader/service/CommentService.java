package loader.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import loader.persistance.Comment;
import loader.persistance.repo.CommentRepo;

@ApplicationScoped
public class CommentService
{
    @Inject
    CommentRepo commentRepo;

    public void handle(Comment comment)
    {
        commentRepo.persist(comment);
    }
}
