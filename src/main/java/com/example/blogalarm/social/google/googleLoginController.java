package com.example.blogalarm.social.google;

import com.example.blogalarm.domain.Member;
import com.example.blogalarm.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@Slf4j
public class googleLoginController {

    @Autowired
    private MemberService memberService;

    //구글 앱에서 Oauth client 설정
    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.pw}")
    private String googleClientPw;


    /*
    URL API
    구글 로그인 URL을 만들어 클라이언트에게 전송하는 메서드
    -> home2의 URL로 대체
     */

//
//    @RequestMapping(value="/api/v1/oauth2/google", method = RequestMethod.POST)
//    public String loginUrlGoogle(){
//        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
//                + "&redirect_uri=http://localhost:8080/api/v1/oauth2/google&response_type=code&scope=email%20profile%20openid&access_type=offline";
//        return reqUrl;
//    }
    /*
           "https://accounts.google.com/o/oauth2/v2/auth?client_id=765904047755-4ak1advmuoofv8r70tqhjmung32gkg4h.apps.googleusercontent.com&redirect_uri=http://localhost:8080/api/v1/oauth2/google&response_type=code&scope=email%20profile%20openid&access_type=offline"
     */
    @RequestMapping(value="/api/v1/oauth2/google", method = RequestMethod.GET)
    public String loginGoogle(@RequestParam(value = "code") String authCode,HttpSession session){

        //ToDo:RestTemplate의 역할?
        //ToDo:ResponseEntity의 역할?

        /*
        restTemplate를 통해 token 정보를 받아오고 해당 id_token 이라는 정보를 한번더 요청하는 형태임

         */


        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri("http://localhost:8080/api/v1/oauth2/google")
                .grantType("authorization_code").build();
        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);
        String jwtToken=resultEntity.getBody().getId_token();
        Map<String, String> map=new HashMap<>();
        map.put("id_token",jwtToken);
        ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfResponse.class);

        String userEmail=resultEntity2.getBody().getEmail();



        Member loggedInMember= (Member) session.getAttribute("loggedInMember");
        Long currentMemberId= loggedInMember.getId();
        if (currentMemberId != null) {
            // Member 객체에 이메일 정보 업데이트
            memberService.updateEmail(currentMemberId, userEmail);
        } else {
            // 적절한 오류 처리
            log.error("현재 로그인한 사용자의 Member 객체를 찾을 수 없습니다.");
            return "현재 로그인한 사용자 정보를 찾을 수 없습니다.";
        }
        return "/home2";
        //redirect를 사용하기 위해선 @ResponseBody가 없어야함


    }
}

/*
현재  구글 로그인 API 연동하는 구조
1. URL 전송
구글 로그인 URL을 클라이언트에게 전송

2. 로그인 UI에서 로그인 하면

3. 자신이 설정한 리다이렉트 URL로 구글에서 token을 제공

4. 토큰 정보 확인
3번에서 발급받은 토큰으로 사용자 정보 확인

 */