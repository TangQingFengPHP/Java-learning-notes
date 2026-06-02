package com.github.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.mybatisplus.mapper")
public class MyBatisPlusPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisPlusPracticeApplication.class, args);
    }
}

