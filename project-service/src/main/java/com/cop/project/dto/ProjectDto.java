package com.cop.project.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {

    @NotBlank(message = "ID cannot be blank.")
    @Schema(description = "Project ID", example = "project-02")
    private String id;

    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 100, message = "Name must be within 100 characters.")
    @Schema(description = "Project name", example = "sample-project-02")
    private String name;

    @NotBlank(message = "Region cannot be blank.")
    @Schema(description = "Region name", example = "us-east-1")
    private String region;

    @NotBlank(message = "Access Key cannot be blank.")
    @Schema(description = "Access key", example = "BKIAJYYFODNN7EXEXAMPLE2")
    private String accessKey;

    @NotBlank(message = "Secret Key cannot be blank.")
    @Schema(description = "Secret key", example = "kJalrXUtnFEMI/Q7MDENG/bPxRfiCYEXAMPLEKEY2")
    private String secretKey;

    @NotNull(message = "Enabled status must be specified.")
    @Schema(description = "Enabled flag", example = "true")
    private Boolean enabled;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Created by user")
    private String createdBy;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Created date")
    private LocalDateTime createdDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Last modified by user")
    private String lastModifiedBy;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Last modified date")
    private LocalDateTime lastModifiedDate;
}
