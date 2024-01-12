package com.example.blogalarm.social.kakao.user.Dto;

import lombok.Data;

@Data
public class KakaoAccount {
    private Boolean has_email;
    private Boolean email_needs_agreement;
    private Boolean is_email_vaild;
    private Boolean is_email_verified;
    private String email;
}
