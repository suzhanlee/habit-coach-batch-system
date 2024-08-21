package com.example.demo.config;

import com.example.demo.domain.model.GptClient;
import com.example.demo.domain.model.LLMClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public LLMClient llmClient() {
        return new GptClient();
    }
}
