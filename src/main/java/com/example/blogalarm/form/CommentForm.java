package com.example.blogalarm.form;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentForm {
    private String text;
    private Long postId;
    private Long commentId;  // 댓글의 ID를 입력받을 필드

}
