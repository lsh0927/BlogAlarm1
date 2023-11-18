package com.example.blogalarm.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String nickname;

    @NotEmpty(message = "비밀번호는 필수 입니다")
    private int password;
}
