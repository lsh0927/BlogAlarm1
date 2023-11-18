package com.example.blogalarm.repository;

import com.example.blogalarm.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl {

    private final EntityManager em;
    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

//    public List<Member> findByNickname(String nickname) {
//        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
//                .setParameter("nickname", nickname)
//                .getResultList();
//    }
//
//    public Optional<Member> findById(Long id) {
//        Member member = em.find(Member.class, id);
//        return Optional.ofNullable(member);
//    }

    public Optional<Member> findByUsername(String username) {
        List<Member> result = em.createQuery("select m from Member m where m.nickname = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
        return result.stream().findFirst();
    }
}