package com.example.blogalarm.controller;

import com.example.blogalarm.domain.Member;
import com.example.blogalarm.form.LoginForm;
import com.example.blogalarm.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    private final MemberService memberService;

    @Autowired
    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인 폼으로 이동
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login/loginForm"; // 로그인 폼 템플릿 이름
    }

    // LoginController 클래스의 login 메서드
    @PostMapping("/home")
    public String login(@ModelAttribute LoginForm loginForm, HttpSession session, HttpServletRequest request) {
        Member member = memberService.login(loginForm, request);

        if (member != null) {
            // 회원 정보가 일치하면 로그인 성공
            session.setAttribute("loggedInMember", member);
            // TODO : /redirect:home2 와 그냥 home2는 뭐가 다른겨?


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


}
