package com.cop.common.openapi;

import java.util.List;

import io.swagger.v3.oas.models.parameters.Parameter;

public record OpenApiGroupConfig(String group, String title, String description, String version,
        String[] packagesToScan, String[] pathsToMatch, List<Parameter> globalHeaders) {

}