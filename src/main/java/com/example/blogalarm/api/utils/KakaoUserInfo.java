package com.example.blogalarm.api.utils;


import com.example.blogalarm.api.user.Dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

//@RequiredArgsConstructor
//@Component
//public class KakaoUserInfo {
//    private final WebClient webClient;
//    private static final String USER_INFO_URI=  "https://kapi.kakao.com/v2/user/me";;
//    //유저 URI는 수정이 필요함
//
//    public KakaoUserInfoResponse getUserInfo(String token){
//        String uri= USER_INFO_URI;
//
//
//        Flux<KakaoUserInfoResponse> response= webClient.get()
//                .uri(uri)
//                .header("Authorization","Bearer"+token)
//                .retrieve()
//                .bodyToFlux(KakaoUserInfoResponse.class);
//
//        return response.blockFirst();
//    }
//}

@RequiredArgsConstructor
@Component
public class KakaoUserInfo {
    private final WebClient webClient;
    private static final String USER_INFO_URI=  "https://kapi.kakao.com/v2/user/me";

    public KakaoUserInfoResponse getUserInfo(String token){
        String uri= USER_INFO_URI;


        Flux<KakaoUserInfoResponse> response= webClient.get()
                .uri(uri)
                .header("Authorization","Bearer "+token)
                .retrieve()
                .bodyToFlux(KakaoUserInfoResponse.class);

        return response.blockFirst();
    }
}
