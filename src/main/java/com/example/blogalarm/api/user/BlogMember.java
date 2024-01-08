//package com.example.blogalarm.api.user;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import jakarta.persistence.*;
//
//@Data
//@NoArgsConstructor
//@Entity // JPA 엔티티임을 나타냄
//@Table(name = "blog_members") // 데이터베이스에서 사용될 테이블 명시
//public class BlogMember {
//
//    @Id // 기본 키임을 나타냄
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 데이터베이스에 위임
//    private Long id;
//
//    @Column(nullable = false, unique = true) // 이메일은 null이 될 수 없고, 고유해야 함
//    private String email;
//
//    @Column(nullable = false) // 닉네임은 null이 될 수 없음
//    private String nickname;
//
//    @Column(nullable = false) // 비밀번호는 null이 될 수 없음
//    private int password;
//
//    public BlogMember( String email, String nickname, int password) {
//        this.email = email;
//        this.nickname = nickname;
//        this.password = password;
//    }
//
//
//
//    // 필요한 추가 메서드나 로직을 여기에 구현할 수 있습니다.
//}
