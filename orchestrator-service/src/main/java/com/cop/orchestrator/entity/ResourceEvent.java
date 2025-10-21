package com.cop.orchestrator.entity;

import java.time.LocalDateTime;

import com.cop.common.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceEvent extends Auditable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "state", nullable = false)
    private String state;
    
}