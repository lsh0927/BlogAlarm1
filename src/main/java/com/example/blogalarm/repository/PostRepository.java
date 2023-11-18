package com.example.blogalarm.repository;

import com.example.blogalarm.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {


    /*
    데이터베이스에서 모든 게시물을 검색하면서 해당 게시물에 속한 댓글들도 함께 로드
     */
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments")
    List<Post> findAllWithComments();

}
