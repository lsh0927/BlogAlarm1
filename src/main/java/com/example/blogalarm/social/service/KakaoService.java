package com.example.blogalarm.social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoService {

    private final WebClient webClient;

    @Autowired
    public KakaoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com").build();
    }


    public void sendKakaoMessage(String accessToken, String message) {
        String jsonBody = "{\"object_type\": \"text\", \"text\": \"" + message +
                "\", \"link\": {\"web_url\": \"http://localhost:8080/posts\", \"mobile_web_url\": \"http://localhost:8080/posts\"}, \"button_title\": \"바로 확인\"}";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("template_object", jsonBody);

        String response = webClient.post()
                .uri("/v2/api/talk/memo/default/send")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response from Kakao API: " + response);
    }
}
