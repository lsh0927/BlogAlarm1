package com.example.blogalarm.jwt;

import com.example.blogalarm.domain.Member;
import com.example.blogalarm.repository.MemberRepository;
import com.example.blogalarm.repository.MemberRepositoryImpl;
import com.example.blogalarm.social.kakao.user.Dto.KakaoTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class MemberTokenService {
    private final MemberTokenRepository memberTokenRepository;
    private final MemberRepository memberRepository;

    public MemberTokenService(MemberTokenRepository memberTokenRepository, MemberRepository memberRepository) {
        this.memberTokenRepository = memberTokenRepository;
        this.memberRepository = memberRepository;
    }

//    public MemberTokenService(MemberTokenRepository memberTokenRepository){
//        this.memberTokenRepository=memberTokenRepository;
//    }
    //만든 멤버토큰 레포지토리 주입

    //멤버 아이디로 토큰을 찾아 유효기간 지났는지 확인후 저장 or 갱신
    public void saveOrUpdateMemberToken(Long memberId, KakaoTokenResponse tokenResponse)
    {
        MemberToken memberToken = memberTokenRepository.findByMemberId(memberId).orElse(new MemberToken());
        //orElse를 안하면 널 에러가 날 수 있음

        //impl말고 그냥 memberRepository를 사용해서 findbyid메서드를 사용할 수 있도록 함
        Member member=memberRepository.findById(memberId).orElseThrow( ()-> new RuntimeException("member not found"));

        log.info("Access token: " + tokenResponse.getAccess_token());
        log.info("Refresh token: " + tokenResponse.getRefresh_token());
        //만료기간을 비교할때, 멤버토큰 클래스의 만료기간 필드들은 초기화 되어 있지 않았어서, 널치리 예외 및 SQL 오류가 발생했었음.
        // 이를 해결하기 위해 코드를 수정해야 함 ( 유효기간 확인 및 초기화 설정)


        // MemberToken 객체에 Member 설정
        memberToken.setMember(member);


        //멤버 토큰의 유효기간 검사
        memberToken.updateAccessToken(tokenResponse.getAccess_token(), calculateExpiry(tokenResponse.getExpires_in()));
        memberToken.updateRefreshToken(tokenResponse.getRefresh_token(), calculateExpiry(tokenResponse.getRefresh_token_expires_in()));

        try {
            memberTokenRepository.save(memberToken);
        } catch (Exception e) {
            // 예외 처리 로직
            log.error("Error saving MemberToken", e);
        }



    }
    private LocalDateTime calculateExpiry(Integer expiresIn) {
        return LocalDateTime.now().plusSeconds(expiresIn != null ? expiresIn : 0);
    }
}
