package com.cop.orchestrator.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cop.common.openapi.BaseOpenApiConfig;

@Configuration
public class OpenApiConfig extends BaseOpenApiConfig {

    @Bean
    public GroupedOpenApi orchestratorV1Api() {
        return buildGroupedOpenApi("orchestrator-v1", 
                "Orchestrator API", "API documentation for orchestrator v1",
                "v1",
                new String[] { "com.cop.orchestrator" },
                new String[] { "/api/v1/**" }, defaultHeaders());

    }

}
