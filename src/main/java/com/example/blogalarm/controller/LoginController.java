package com.example.blogalarm.controller;
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

    private final MemberRepositoryImpl memberRepository;
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

        //로그인 API 호출(html 코드에 /home 으로 post요청을 하도록되어 있음
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
   // @ResponseBody
    public String kakaoOauth(@RequestParam("code") String code,HttpServletRequest request) {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        log.info("회원 정보 입니다.{}",userInfo);

        String userEmail = userInfo.getKakao_account().getEmail();
        //HttpSession session = request.getSession();
//        log.info("세션 정보.",session.getId());

        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
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

    @GetMapping("/naver/callback")
    public String callback(HttpServletRequest request) throws Exception{
        NaverDTO naverInfo = naverService.getNaverInfo(request.getParameter("code"));

        String userEmail= naverInfo.getEmail();


        System.out.println("유저이메일" + userEmail);
        System.out.println("세션 id:" +session.getId());

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

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String login(Model model){
//        model.addAttribute("naverUrl", naverService.getNaverLogin());
//        return "index2";
//    }



}
