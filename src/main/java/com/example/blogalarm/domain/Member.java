package com.example.blogalarm.domain;

import com.example.blogalarm.jwt.MemberToken;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//Redis를 사용하기 위함임

//그런데 소셜 로그인을 할때 엑세스 토큰을 받아오고 이를 레디스에 저장하기 위함인데, 처음부터 멤버를 직렬가능하도록 해야하는 이유가 무엇인가..?
public class Member implements Serializable {

    //필드
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Column(length = 6)
    private int password;

    private String email;

    //멤버와 댓글,포스트간 연관관계 매핑
    @OneToMany(mappedBy = "member")
    private List<Post> posts= new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments= new ArrayList<>();

    //jwt 토큰과 연관 관계 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberToken token;
}
