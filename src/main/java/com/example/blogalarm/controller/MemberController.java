package com.example.blogalarm.controller;

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

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }


    @PostMapping("/members/new")
    public String createMember(MemberForm memberForm) {
        // MemberForm을 Member 엔티티로 변환
        Member member = new Member();
        member.setNickname(memberForm.getNickname()); // 'name' 대신 'nickname'을 사용합니다.

        // 나머지 Member 엔티티의 필드도 설정
        member.setPassword(memberForm.getPassword());

        // 회원 저장
        memberService.join(member);

        // 회원 목록으로 리다이렉트
        return "redirect:/";
    }

    @GetMapping("/members/memberList")
    public String listMembers(Model model) {
        List<Member> members = memberService.findMembers(); // 모든 회원 정보를 가져옴
        model.addAttribute("members", members);
        return "members/memberList"; // 회원 목록을 보여줄 템플릿 이름
    }


    @GetMapping("/members/edit/{memberId}")
    public String editForm(@PathVariable Long memberId, Model model) {
        // memberId가 null 또는 0보다 작거나 같은 경우, 유효하지 않은 값으로 간주하고 처리합니다.
        if (memberId == null || memberId <= 0) {
            // 이런 경우, 회원 목록 페이지로 리다이렉트합니다.
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
