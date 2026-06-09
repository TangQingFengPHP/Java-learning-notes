package com.github.tomcat.service;

import com.github.tomcat.model.TomcatInfoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TomcatInfoService {

    private final Environment environment;
    private final ObjectProvider<ServletWebServerApplicationContext> webServerContextProvider;

    public TomcatInfoDTO info() {
        int port = environment.getProperty("server.port", Integer.class, 8080);
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        if (contextPath == null || contextPath.isBlank()) {
            contextPath = "/";
        }

        int maxThreads = environment.getProperty("server.tomcat.threads.max", Integer.class, 200);
        int minSpareThreads = environment.getProperty("server.tomcat.threads.min-spare", Integer.class, 10);
        int acceptCount = environment.getProperty("server.tomcat.accept-count", Integer.class, 100);
        long connectionTimeoutMs = parseDurationMs(
                environment.getProperty("server.tomcat.connection-timeout", "20000ms"));

        String deploymentMode = environment.getProperty("DEPLOYMENT_MODE", "embedded");
        ServletWebServerApplicationContext webServerContext = webServerContextProvider.getIfAvailable();
        if (webServerContext != null && webServerContext.getWebServer() instanceof TomcatWebServer tomcatWebServer) {
            deploymentMode = "embedded";
            Connector connector = tomcatWebServer.getTomcat().getConnector();
            if (connector != null) {
                port = connector.getPort();
                Object maxThreadsProperty = connector.getProperty("maxThreads");
                if (maxThreadsProperty instanceof Integer value) {
                    maxThreads = value;
                }
                Object minSpareProperty = connector.getProperty("minSpareThreads");
                if (minSpareProperty instanceof Integer value) {
                    minSpareThreads = value;
                }
                Object timeoutProperty = connector.getProperty("connectionTimeout");
                if (timeoutProperty instanceof Integer value) {
                    connectionTimeoutMs = value;
                }
            }
        }

        return new TomcatInfoDTO(
                deploymentMode,
                port,
                contextPath,
                maxThreads,
                minSpareThreads,
                acceptCount,
                connectionTimeoutMs
        );
    }

    private long parseDurationMs(String value) {
        if (value == null || value.isBlank()) {
            return 20_000L;
        }
        String normalized = value.trim().toLowerCase();
        if (normalized.endsWith("ms")) {
            return Long.parseLong(normalized.substring(0, normalized.length() - 2));
        }
        if (normalized.endsWith("s")) {
            return Long.parseLong(normalized.substring(0, normalized.length() - 1)) * 1000L;
        }
        return Long.parseLong(normalized);
    }
}
