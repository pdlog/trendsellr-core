package com.trendsellr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseTestIT {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Component
    static class DefaultTestHeaders {

        private final String testApiKey;

        public DefaultTestHeaders(@Value("${api.security.api-key}") final String testApiKey) {
            this.testApiKey = testApiKey;
        }

        @Bean
        public MockMvcBuilderCustomizer defaultHeaders() {
            return builder -> builder.defaultRequest(
                    get("/")
                            .header("X-API-KEY", this.testApiKey)
            );
        }
    }
}