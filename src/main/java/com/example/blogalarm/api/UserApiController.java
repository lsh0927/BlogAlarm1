package com.example.blogalarm.api;


import com.example.blogalarm.api.user.Dto.SignupRequestDto;
import com.example.blogalarm.api.user.UserService;
import com.example.blogalarm.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    private final MemberService memberService;

    //카카오 로그인 후 회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto){

        Long userId= userService.createUser(requestDto.getEmail());
        Long memberId= memberService.createMemberWithUserInfo
                (requestDto.getEmail(), requestDto.getNickname(), requestDto.getPassword());


                return  ResponseEntity.ok().build();
    }
}
