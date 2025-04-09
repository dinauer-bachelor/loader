package dev.dinauer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.dinauer.persistence.entity.Comment;
import dev.dinauer.persistence.repo.CommentRepo;

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
