package com.example.blogalarm.social.naver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverDTO {
    private String id;
    private String email;
    private String name;
}
