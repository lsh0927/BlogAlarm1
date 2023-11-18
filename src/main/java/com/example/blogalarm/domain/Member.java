package com.example.blogalarm.domain;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    //필드
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Column(length = 6)
    private int password;


    //멤버와 댓글,포스트간 연관관계 매핑
    @OneToMany(mappedBy = "member")
    private List<Post> posts= new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments= new ArrayList<>();

}
