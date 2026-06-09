package com.github.tomcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.github.tomcat.servlet")
public class TomcatPracticeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TomcatPracticeApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TomcatPracticeApplication.class);
    }
}
