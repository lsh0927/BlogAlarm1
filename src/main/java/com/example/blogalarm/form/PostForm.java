package com.example.blogalarm.form;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostForm {

    private Long id; // id 필드의 이름을 postId로 변경
    private String title;
    private String content;
}
