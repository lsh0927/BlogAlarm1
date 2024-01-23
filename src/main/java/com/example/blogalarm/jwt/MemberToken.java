package com.example.blogalarm.jwt;

import com.example.blogalarm.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class MemberToken {
    // 명시적으로 생성자 추가
    public MemberToken() {
        this.accessTokenExpiry = LocalDateTime.now().plusMinutes(60);  // 예: 60분 후로 설정
        this.refreshTokenExpiry = LocalDateTime.now().plusDays(30);  // 예: 30일 후로 설정
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime accessTokenExpiry;

    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiry;


    public void updateAccessToken(String accessToken, LocalDateTime expiry) {
        this.accessToken = accessToken;
        this.accessTokenExpiry = (expiry != null) ? expiry : LocalDateTime.now().plusMinutes(60);
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiry) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = (expiry != null) ? expiry : LocalDateTime.now().plusDays(30);
    }


    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(accessTokenExpiry);
    }

    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(refreshTokenExpiry);
    }
}