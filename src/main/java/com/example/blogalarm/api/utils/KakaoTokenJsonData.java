package com.example.blogalarm.api.utils;

import com.example.blogalarm.api.user.Dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
//
//@Component
//@RequiredArgsConstructor
//public class KakaoTokenJsonData {
//
//    //내 어플리케이션의 정보를 가져와서 수정해야 함
//    private final WebClient webClient;
//    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
//    private static final String REDIRECT_URI = "https://127.0.0.1:4444/kakaocallback";
//    private static final String GRANT_TYPE = "authorization_code";
//    private static final String CLIENT_ID = "db6db7e1326d0dc20972500f2d0d813f";
//
//    public KakaoTokenResponse getToken(String code){
//        String uri= TOKEN_URI + "?grant_type=" + GRANT_TYPE + "&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&code=" + code;
//        System.out.println(uri);
//
//        Flux<KakaoTokenResponse> response= webClient.post()
//                .uri(uri)
//                .contentType(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToFlux(KakaoTokenResponse.class);
//
//        return response.blockFirst();
//    }
//}

@Component
@RequiredArgsConstructor
public class KakaoTokenJsonData {

    //내 어플리케이션의 정보를 가져와서 수정해야 함
    private final WebClient webClient;
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String REDIRECT_URI = "http://127.0.0.1:8080/kakaocallback";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID = "db6db7e1326d0dc20972500f2d0d813f";

    public KakaoTokenResponse getToken(String code){
        String uri= TOKEN_URI + "?grant_type=" + GRANT_TYPE + "&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&code=" + code+"&scope=talk_message";
        System.out.println(uri);

        Flux<KakaoTokenResponse> response= webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoTokenResponse.class);

        return response.blockFirst();
    }
}
