package com.example.blogalarm.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import jakarta.servlet.http.HttpSession;

@Getter
@Setter
public class MemberForm {


    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String nickname;

    @NotEmpty(message = "비밀번호는 필수 입니다")
    private int password;

    private String email;

}
