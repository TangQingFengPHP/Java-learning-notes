package com.github.mavenpractice.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/maven")
public class MavenInfoController {

    private final Optional<BuildProperties> buildProperties;

    @Value("${app.meta.version:unknown}")
    private String appVersion;

    @Value("${app.meta.env:unknown}")
    private String appEnv;

    public MavenInfoController(Optional<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("artifact", "maven-practice");
        result.put("module", "maven-web");
        result.put("packaging", "jar");
        result.put("filteredVersion", appVersion);
        result.put("profileEnv", appEnv);
        buildProperties.ifPresent(bp -> {
            result.put("buildVersion", bp.getVersion());
            result.put("buildTime", bp.getTime().toString());
        });
        return result;
    }
}
