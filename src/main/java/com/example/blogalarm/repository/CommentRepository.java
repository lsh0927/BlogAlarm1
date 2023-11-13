package com.example.blogalarm.repository;

import com.example.blogalarm.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 메서드 추가
}
