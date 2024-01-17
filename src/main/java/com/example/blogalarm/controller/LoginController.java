package com.example.blogalarm.controller;
import com.example.blogalarm.social.google.GoogleInfResponse;
import com.example.blogalarm.social.google.GoogleRequest;
import com.example.blogalarm.social.google.GoogleResponse;
import com.example.blogalarm.social.kakao.user.Dto.KakaoTokenResponse;
import com.example.blogalarm.social.kakao.user.Dto.KakaoUserInfoResponse;
import com.example.blogalarm.social.kakao.utils.KakaoTokenJsonData;
import com.example.blogalarm.social.kakao.utils.KakaoUserInfo;
import com.example.blogalarm.domain.Member;
import com.example.blogalarm.form.LoginForm;
import com.example.blogalarm.repository.MemberRepositoryImpl;
import com.example.blogalarm.service.MemberService;
import com.example.blogalarm.social.naver.NaverService;
import com.example.blogalarm.social.naver.dto.NaverDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
@Controller
@CrossOrigin("*")
public class LoginController {
    private final MemberService memberService;
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoUserInfo kakaoUserInfo;
    private final NaverService naverService;

   @Autowired
   private HttpSession session;

    // 로그인 폼으로 이동
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "/login/loginForm"; // 로그인 폼 템플릿 이름

        //로그인 API 호출(html 코드에 /home 으로 post요청을 하도록 되어 있음
    }

    // LooginController 클래스의 login 메서드
    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, HttpServletRequest request) {

        Member member = memberService.login(loginForm, request);
        if (member != null) {
            // 회원 정보가 일치하면 로그인 성공
            session= request.getSession();
            session.setAttribute("loggedInMember", member);
            // TODO : /redirect:home2 와 그냥 home2는 뭐가 다른지?

            log.info("세션정보입니다:"+String.valueOf(session));
            log.info(session.getId());
            return  "/home2";
        } else {
            System.out.println("해당 회원 정보가 없습니다");
            return "login/loginForm";
        }
    }
    @GetMapping("/home")
    public String page(Model model) {
        return "home2"; // 로그인 폼 템플릿 이름
    }


    @Description("회원이 소셜 로그인을 마치면 자동으로 실행되는 API입니다. 인가 코드를 이용해 토큰을 받고, 해당 토큰으로 사용자 정보를 조회합니다." +
            "사용자 정보를 이용하여 서비스에 회원가입합니다.")

    @GetMapping("/kakaocallback")
    public String kakaoOauth(@RequestParam("code") String code,HttpServletRequest request) {
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
        // 카카오 서버에 Oauth 토큰 요청

        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        String userEmail = userInfo.getKakao_account().getEmail();

        /*
        밑의 두 방식과 다르게, webClient 방식을 이용하여 데이터를 받아옴.
         */

        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        Long currentMemberId= loggedInMember.getId();
        if (currentMemberId != null) {
            // Member 객체에 이메일 정보 업데이트
            memberService.updateEmail(currentMemberId, userEmail);
        } else {
            return "현재 로그인한 사용자 정보를 찾을 수 없습니다.";
        }
        return "/home2";

        //redirect를 사용하기 위해선 @ResponseBody가 없어야함

        /*
            WebClient는 비동기적 + 논 블로킹 방식으로 작동
            요청을 보내고 응답을 기다리는 동안 다른 작업을 수행할 수 있음을 의미
            RestTemplate과는 다르게, webClient는 스레드를 차단 하지않아 효율적인 자원 사용

            리액티브 프로그래밍: webclient는 리액티브 프로그래밍 패러다임을 따름
            -> Flux와 Mono를 반환-> 비동기적인 데이터 흐름을 다룸( 리액티브 스트림의 일부)

            스트림 처리: Flux는 0개 이상의 데이터를 처리할 수 있는 리액티브 타입
            -> 이를 통해 여러 데이터의 흐름을 비동기적으로 처리할 수 잇음
         */

    }

    @GetMapping("/naver/callback")
    public String callback(HttpServletRequest request) throws Exception{
        NaverDTO naverInfo = naverService.getNaverInfo(request.getParameter("code"));

        String userEmail= naverInfo.getEmail();

        Member loggedInMember= (Member) session.getAttribute("loggedInMember");
        Long currentMemberId= loggedInMember.getId();
        if (currentMemberId != null) {
            // Member 객체에 이메일 정보 업데이트
            memberService.updateEmail(currentMemberId, userEmail);
        } else {
            return "현재 로그인한 사용자 정보를 찾을 수 없습니다.";
        }
        return "/home2";
    }

    //구글 앱에서 Oauth client 설정
    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.pw}")
    private String googleClientPw;

    @RequestMapping(value="/api/v1/oauth2/google", method = RequestMethod.GET)
    public String loginGoogle(@RequestParam(value = "code") String authCode,HttpSession session){

        RestTemplate restTemplate = new RestTemplate();

        //Http 요청을 보내고 받기 위한 스프링의 도구
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
            memberService.updateEmail(currentMemberId, userEmail);
        } else {
            return "현재 로그인한 사용자 정보를 찾을 수 없습니다.";
        }
        return "/home2";

        /*
        @RequestMapping-> Value="code" String authCode는
         Oauth 인증과정에서 구글로부터 받은 code 매개변수, HttpSession은 브라우저에 저장된 회원의 세션을 뜻함
        */



        //RestTemplate의 역할?
         /*
         HTTP 통신을 위한 도구로
         RESTful API 웹 서비스와의 상호작용을 쉽게
         외부 도메인에서 데이터를 가져오거나 전송할 때 사용되는
         스프링 프레임워크의 클래스를 의미
         */

        //ResponseEntity의 역할?
        // @(Rest)Controller 에서Restful API에서 클라이언트에게 보다 세밀한 응답 제어를 가능하게 함
        /*
        restTemplate를 통해 token 정보를 받아오고 해당 id_token 이라는 정보를 한번더 요청하는 형태임

        정리: RestTemplaet은 외부 API와의 통신, ResponseEntity는 API응답의 처리를 담당
         */

        /*postForEntity 메서드를 통해 HTTP POST 요청을 보내고 그 결과를 ResponseEntity
        객체로 받음=> 3개의 매개변수를 받아야함. URL, 요청 본문, 그 타입

        url=> 구글 Oauth 2.0 서버의 토큰 엔드포인트
        (요청이 도달해야하는 서버의 위치 지정)

        본문=> 토큰 요청에 필요한 데이터 포함
        */
    }
}
