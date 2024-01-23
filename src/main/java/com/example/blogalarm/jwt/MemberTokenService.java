package com.example.blogalarm.jwt;

import com.example.blogalarm.social.kakao.user.Dto.KakaoTokenResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberTokenService {
    private final MemberTokenRepository memberTokenRepository;

    public MemberTokenService(MemberTokenRepository memberTokenRepository){
        this.memberTokenRepository=memberTokenRepository;
    }
    //만든 멤버토큰 레포지토리 주입

    //멤버 아이디로 토큰을 찾아 유효기간 지났는지 확인후 저장 or 갱신
    public void saveOrUpdateMemberToken(Long memberId, KakaoTokenResponse tokenResponse)
    {
        MemberToken memberToken = memberTokenRepository.findByMemberId(memberId).orElse(new MemberToken());
        //orElse를 안하면 널 에러가 날 수 있음

        //멤버 토큰의 유효기간 검사
        memberToken.updateAccessToken(tokenResponse.getAccess_token(),calculateExpiry(tokenResponse.getExpires_in()));
        memberToken.updateRefreshToken(tokenResponse.getRefresh_token(),calculateExpiry(tokenResponse.getRefresh_token_expires_in()));
    }
    private LocalDateTime calculateExpiry(Integer expiresIn) {
        return LocalDateTime.now().plusSeconds(expiresIn != null ? expiresIn : 0);
    }
}
