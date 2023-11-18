package com.example.blogalarm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter @Setter
public class Comment {

    //댓글의 내용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    //연관관계 매핑

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;





    //댓글을 단 유저의 이름(Member_Id를 이용해 알아내려 했으나 실패, 포스트의 댓글에 member의 정보가 미지수임을 출력
    public String getMemberName() {
        return member != null ? member.getNickname() : "Unknown Member";
    }




}

