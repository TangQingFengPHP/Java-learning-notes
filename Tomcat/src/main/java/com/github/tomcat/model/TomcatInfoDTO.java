package com.github.tomcat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TomcatInfoDTO {
    private String deploymentMode;
    private int port;
    private String contextPath;
    private int maxThreads;
    private int minSpareThreads;
    private int acceptCount;
    private long connectionTimeoutMs;
}
