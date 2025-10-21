package com.cop.project.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cop.common.openapi.BaseOpenApiConfig;

@Configuration
public class OpenApiConfig extends BaseOpenApiConfig {

    @Bean
    public GroupedOpenApi projectV1Api() {
        return buildGroupedOpenApi("project-v1",
                "Project API V1", "API documentation for Project V1",
                "v1",
                new String[] { "com.cop.project" },
                new String[] { "/api/v1/**" });
    }

    @Bean
    public GroupedOpenApi projectV2Api() {
        return buildGroupedOpenApi("project-v2",
                "Project API V2",
                "Updated API documentation for Project V2", "v2",
                new String[] { "com.cop.project" },
                new String[] { "/api/v2/**" });
    }
}
