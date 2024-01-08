package com.example.blogalarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

public class BlogAlarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogAlarmApplication.class, args);

    }


}
