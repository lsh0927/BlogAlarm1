package com.example.blogalarm.api.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService{
    private final UserRespository userRespository;

    @Transactional
    public Long createUser(String email){
        User user=User.builder()
                .email(email)
                .build();

        userRespository.save(user);
        log.info("새로운 회원 저장 완료");
        return user.id;
    }
    // 이메일로 사용자 검색
    public User findUserByEmail(String email) {
        return userRespository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email));
    }

    public User getUserByEmail(String userEmail) {
        return findUserByEmail(userEmail);
    }
}