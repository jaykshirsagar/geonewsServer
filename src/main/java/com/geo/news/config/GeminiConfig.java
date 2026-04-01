package com.geo.news.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

@Configuration
public class GeminiConfig {
    @Value("${google.api.key}")
    private String apiKey;

    @Bean
    @Conditional(GeminiApiKeyConfiguredCondition.class)
    public Client geminiClient() {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }

    static final class GeminiApiKeyConfiguredCondition implements org.springframework.context.annotation.Condition {
        @Override
        public boolean matches(org.springframework.context.annotation.ConditionContext context,
                               AnnotatedTypeMetadata metadata) {
            String configuredKey = context.getEnvironment().getProperty("google.api.key", "");
            return StringUtils.hasText(configuredKey);
        }
    }
}
