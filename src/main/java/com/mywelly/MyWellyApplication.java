package com.mywelly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyWellyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyWellyApplication.class, args);
    }
}
