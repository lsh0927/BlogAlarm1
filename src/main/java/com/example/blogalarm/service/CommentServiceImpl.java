package com.example.blogalarm.service;

import com.example.blogalarm.domain.Comment;
import com.example.blogalarm.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ApplicationEventPublisher eventPublisher) {
        this.commentRepository = commentRepository;
    }


    // 포스트에 대한 댓글을 가져오는 로직 구현
    @Override
    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }


    //댓글 목록을 보여주는 로직 구현
    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    //Id로 댓글 찾기 기능
    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    //댓글 저장
    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    //댓글 수정
    @Override
    public void updateComment(Comment comment) {
        commentRepository.save(comment);
    }

    //댓글 삭제
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
