package com.example.blogalarm.jwt;

import com.example.blogalarm.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class MemberToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
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
        this.accessTokenExpiry = expiry;
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiry) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = expiry;
    }

    public void CheckTokenExpiry(LocalDateTime accessTokenExpiry, LocalDateTime refreshTokenExpiry){

    }
}