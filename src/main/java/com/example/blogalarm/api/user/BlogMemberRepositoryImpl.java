package com.example.blogalarm.api.user;

import com.example.blogalarm.api.user.Dto.SignupRequestDto;
import com.example.blogalarm.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class BlogMemberRepositoryImpl {
    private final EntityManager em;

    public BlogMemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }
    public void save(BlogMember blogMember) {
        em.persist(blogMember);
    }

}
