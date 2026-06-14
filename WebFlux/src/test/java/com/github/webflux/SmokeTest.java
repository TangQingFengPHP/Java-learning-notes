package com.github.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.r2dbc.url=r2dbc:h2:mem:///webflux_demo;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.r2dbc.username=sa",
        "spring.r2dbc.password=",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:schema.sql",
        "app.user-api.base-url=http://localhost:8190"
})
class SmokeTest {

    @Test
    void contextLoads() {
    }
}
