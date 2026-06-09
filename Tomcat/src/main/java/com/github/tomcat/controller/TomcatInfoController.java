package com.github.tomcat.controller;

import com.github.tomcat.model.TomcatInfoDTO;
import com.github.tomcat.service.TomcatInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tomcat")
@RequiredArgsConstructor
public class TomcatInfoController {

    private final TomcatInfoService tomcatInfoService;

    @GetMapping("/info")
    public TomcatInfoDTO info() {
        return tomcatInfoService.info();
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello Tomcat (Spring MVC)");
    }
}
