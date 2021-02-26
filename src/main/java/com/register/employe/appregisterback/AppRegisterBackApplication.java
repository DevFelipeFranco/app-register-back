package com.register.employe.appregisterback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppRegisterBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppRegisterBackApplication.class, args);
    }

}
