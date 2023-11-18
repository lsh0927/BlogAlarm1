package com.example.blogalarm.service;

import com.example.blogalarm.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsForPost(Long postId);

    List<Comment> getAllComments();
    Comment getCommentById(Long id);
    void saveComment(Comment comment);
    void updateComment(Comment comment);
    void deleteComment(Long id);
}
