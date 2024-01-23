package com.example.blogalarm.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTokenRepository extends JpaRepository<MemberToken,Long> {

    Optional<MemberToken> findByMemberId(Long memberId);
}
