package com.example.blogalarm.controller;

//import com.example.blogalarm.api.user.*;
//import com.example.blogalarm.api.user.Dto.SignupRequestDto;
import com.example.blogalarm.domain.Member;
import com.example.blogalarm.form.MemberForm;
import com.example.blogalarm.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
//    private final UserService userService;


    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
        //기본 회원 가입 로직에 카카오 이메일까지 함께 받도록

        //-> 로그인은 먼저 하고, 이메일(카카오 연동까지 하시겠습니까?) 선택시 푸시 알림을 받을 수 있도록
        //그럼 여기에 입력하면
    }


    @PostMapping("/members/new")
    public String createMember(MemberForm memberForm) {
        // MemberForm을 Member 엔티티로 변환
        Member member = new Member();
        member.setNickname(memberForm.getNickname()); // 'name' 대신 'nickname' 을 사용합니다.
        // 나머지 Member 엔티티의 필드도 설정
        member.setPassword(memberForm.getPassword());



        //이메일을 회원 정보로 추가하기 위해 Dto에서 추출한뒤 blogMemberService에서
        //이걸 이용해 blogMember 정보를 만들어내는 구조 -> 수정하여 Member에게 바로 전달 할 수 있도록 해야함
        //signupRequestDto.setEmail(user.getEmail());



        //member에게 이메일 정보를 받아오기 위해 user 객체 생성
        //이 항목은 여기서 하는게 아니라, 이메일 수집 동의시 다른 웹 페이지에서 받아 들여 멤버 정보에 추가하도록 해야함
        // em.persist에서 그럼 회원정보를 가지고만 있다가 나중에 이메일을 받으면 한꺼번에 persist하도록 해야하나???

//        User user= userService.getUserByEmail(userEmail);
//        member.setEmail(user.getEmail());

        // 회원 저장
        memberService.join(member);



        // 회원 목록으로 리다이렉트 -> 회원가입/로그인 중 택 1로 선택하는 페이지
        return "redirect:/";
    }

    @GetMapping("/members/memberList")
    public String listMembers(Model model) {
        List<Member> members = memberService.findMembers(); // 모든 회원 정보를 가져옴
        model.addAttribute("members", members);
        return "members/memberList"; // 회원 목록을 보여줄 템플릿 이름

        //여기서 부터 모든 멤버를 블로그 멤버로 바꿔서 행동하도록 바꿔야 하나

    }


    @GetMapping("/members/edit/{memberId}")
    public String editForm(@PathVariable Long memberId, Model model) {
        // memberId가 null 또는 0보다 작거나 같은 경우, 유효하지 않은 값으로 간주하고 처리.
        if (memberId == null || memberId <= 0) {
            // 이런 경우, 회원 목록 페이지로 리다이렉트.
            return "redirect:/members/memberList"; // 회원 목록 페이지로 리다이렉트
        }

        // memberId가 유효한 경우에만 회원 정보 조회 및 폼 표시
        Member member = memberService.findOne(memberId);



        // MemberForm 객체를 생성하고 모델에 추가
        MemberForm memberForm = new MemberForm();
        memberForm.setNickname(member.getNickname());
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("member", member);
        return "members/editMemberForm";
    }


    @PostMapping("/members/edit/{memberId}")
    public String editMember(@PathVariable Long memberId, MemberForm memberForm) {
        // memberId가 null 또는 0보다 작거나 같은 경우, 유효하지 않은 값으로 간주하고 처리합니다.
        if (memberId == null || memberId <= 0) {
            // 이런 경우, 회원 목록 페이지로 리다이렉트합니다.
            return "redirect:/members/memberList"; // 회원 목록 페이지로 리다이렉트
        }

        Member member = new Member();
        member.setId(memberId); // 기존 회원의 ID를 설정


        // 업데이트할 필드 설정
        member.setNickname(memberForm.getNickname());
        member.setPassword(memberForm.getPassword());
        memberService.update(memberId, member.getNickname(), memberForm.getPassword()); // 회원 정보 수정 서비스 호출
        return "redirect:/members/memberList"; // 회원 목록 페이지로 리다이렉트
    }
}


