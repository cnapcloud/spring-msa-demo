package com.cop.orchestrator.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VpcRequestDto {

    @Schema(description = "Request Id", accessMode = Schema.AccessMode.READ_ONLY)
    private String requestId;

    @Schema(description = "Region", accessMode = Schema.AccessMode.READ_ONLY)
    private String region;

    @NotBlank(message = "VPC name cannot be blank")
    @Size(max = 255, message = "Name cannot be longer than 255 characters")
    @Schema(example = "my-vpc")
    private String vpcName;

    @NotBlank(message = "CIDR Block cannot be blank")
    @Pattern(regexp = "^(([0-9]{1,3}\\.){3}[0-9]{1,3})/(3[0-2]|[1-2][0-9]|[0-9])$", message = "CIDR Block must be a valid format (e.g., 192.168.0.1/24)")
    @Schema(example = "10.0.0.0/16")
    private String cidrBlock;

    @Schema(description = "Project Id", accessMode = Schema.AccessMode.READ_ONLY)
    private String projectId;

    @Schema(description = "Workflow Id", accessMode = Schema.AccessMode.READ_ONLY)
    private String workflowId;

    @Schema(description = "Created By", accessMode = Schema.AccessMode.READ_ONLY)
    private String createdBy;

    @Schema(description = "Created Date", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdDate;

    @Schema(description = "Last Modified By", accessMode = Schema.AccessMode.READ_ONLY)
    private String lastModifiedBy;

    @Schema(description = "Last Modified Date", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastModifiedDate;
}
