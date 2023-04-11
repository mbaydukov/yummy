package org.yummy.recipes.config;

import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import jakarta.annotation.PostConstruct;
import java.time.Duration;

@Component
public class Application {

    @PostConstruct
    void initialize() {
    }

    public WebTestClient testClient(int port) {
        return WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(300))
                .baseUrl("http://localhost:" + port)
                .build();
    }
}
