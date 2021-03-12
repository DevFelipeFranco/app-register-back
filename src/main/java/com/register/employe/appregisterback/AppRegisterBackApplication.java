package com.register.employe.appregisterback;

import com.register.employe.appregisterback.domain.constants.FileConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

import static com.register.employe.appregisterback.domain.constants.FileConstant.USER_FOLDER;

@SpringBootApplication
@EnableAsync
public class AppRegisterBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppRegisterBackApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

}
