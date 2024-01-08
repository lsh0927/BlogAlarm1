package com.example.blogalarm.api.user;

import com.example.blogalarm.api.user.Dto.SignupRequestDto;
import com.example.blogalarm.domain.Member;
import com.example.blogalarm.form.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogMemberService {
    private final BlogMemberRepositoryImpl blogMemberRepository;

    @Transactional
    public Long createBlogMember(SignupRequestDto signupRequestDto) {
        // SignupRequestDto에서 필요한 정보 추출
        String email = signupRequestDto.getEmail();
        String nickname = signupRequestDto.getNickname();
        int password = signupRequestDto.getPassword();

        // 필요한 로직 수행, 예를 들어 BlogMember 엔티티 생성 및 저장
        BlogMember blogMember = new BlogMember(email, nickname, password);
        blogMemberRepository.save(blogMember);
        log.info("새로운 블로그 회원 저장 완료");

        // 저장된 엔티티의 ID 반환
        return blogMember.getId();
    }

}
