package com.cop.common.openapi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

public abstract class BaseOpenApiConfig {

    protected GroupedOpenApi buildGroupedOpenApi(String group, String title, String description, String version,
            String[] packagesToScan, String[] pathsToMatch) {

        return buildGroupedOpenApi(group, title, description, version, packagesToScan, pathsToMatch, List.of());
    };

    protected GroupedOpenApi buildGroupedOpenApi(String group, String title, String description, String version,
            String[] packagesToScan, String[] pathsToMatch, List<Parameter> globalHeaders) {
        OpenApiGroupConfig config = new OpenApiGroupConfig(group, title, description, version, packagesToScan,
                pathsToMatch, globalHeaders);

        return GroupedOpenApi.builder().group(config.group()).packagesToScan(config.packagesToScan())
                .pathsToMatch(config.pathsToMatch())
                .addOpenApiCustomizer(buildGlobalHeaderCustomizer(config.globalHeaders()))
                .addOpenApiCustomizer(openApi -> openApi.info(buildOpenAPI(config).getInfo())).build();
    }

    protected String securitySchemeName() {
        return "bearerAuth";
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    protected OpenAPI buildOpenAPI(OpenApiGroupConfig config) {
        return new OpenAPI()
                .info(new Info().title(config.title()).version(config.version()).description(config.description()))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName()))
                .components(new Components().addSecuritySchemes(securitySchemeName(),
                        new SecurityScheme().name(securitySchemeName()).type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")));
    }

    protected OpenApiCustomizer buildGlobalHeaderCustomizer(List<Parameter> globalHeaders) {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                List<Parameter> existingParams = operation.getParameters();
                if (existingParams == null) {
                    operation.setParameters(new ArrayList<>(globalHeaders));
                } else {
                    List<Parameter> filtered = existingParams.stream()
                            .filter(p -> globalHeaders.stream().noneMatch(g -> g.getName().equals(p.getName())))
                            .collect(Collectors.toList());

                    List<Parameter> newParams = new ArrayList<>();
                    newParams.addAll(globalHeaders);
                    newParams.addAll(filtered);
                    operation.setParameters(newParams);
                }
            }));
        };
    }

    protected List<Parameter> defaultHeaders() {
        Parameter userIdHeader = new Parameter().in("header").required(true).name("UserID").description("User ID")
                .example("user-123").schema(new StringSchema());

        Parameter requestIdHeader = new Parameter().in("header").required(true).name("RequestID")
                .description("Tracking ID").example("req-456").schema(new StringSchema());

        return List.of(userIdHeader, requestIdHeader);
    }
}
