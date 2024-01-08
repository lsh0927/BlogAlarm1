package com.example.blogalarm.api.user;

import com.example.blogalarm.api.user.Dto.SignupRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogMemberRepository extends JpaRepository<SignupRequestDto,String> {
}
