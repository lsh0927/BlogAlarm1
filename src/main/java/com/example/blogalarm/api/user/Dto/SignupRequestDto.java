package com.example.blogalarm.api.user.Dto;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blog_memeber_table")
@NoArgsConstructor
@Getter
public class SignupRequestDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private int password; // 실제로는 String 타입을 사용하고, 보안을 위해 암호화하는 것이 일반적입니다.

    // 각 필드에 대한 getter 및 setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
    @Builder
    public SignupRequestDto(Long id,String email,String nickname, int password) {
        this.id=id;
        this.email=email;
        this.nickname=nickname;
        this.password=password;
    }
}

