package com.example.blogalarm.social;


import com.example.blogalarm.social.naver.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@Controller
public class SocialController {


    private final NaverService naverService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute("naverUrl", naverService.getNaverLogin());
        return "index2";
    }
}
