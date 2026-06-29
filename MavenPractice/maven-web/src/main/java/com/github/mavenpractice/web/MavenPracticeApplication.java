package com.github.mavenpractice.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.mavenpractice")
public class MavenPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MavenPracticeApplication.class, args);
    }
}
