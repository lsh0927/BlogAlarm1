package com.example.blogalarm.repository;

import com.example.blogalarm.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    // 포스트에 대한 댓글을 가져오는 로직 구현을 위해
    List<Comment> findAllByPostId(Long postId);


}
