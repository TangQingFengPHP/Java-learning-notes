package com.github.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringDataJpaPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaPracticeApplication.class, args);
    }
}
