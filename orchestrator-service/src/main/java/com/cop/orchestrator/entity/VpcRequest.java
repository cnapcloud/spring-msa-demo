package com.cop.orchestrator.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.cop.common.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vpc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VpcRequest extends Auditable {

    @Id
    private String requestId;

    @Column(name = "cidr_block", nullable = false)
    private String cidrBlock;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "vpc_name", nullable = false)
    private String vpcName;
    
    @Column(name = "project_id", nullable = false)
    private String projectId;
       
    @Column(name = "workflowId", nullable = true)
    private String workflowId;  
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "result", nullable = true, columnDefinition = "json")
    private Map<String, Object> result;

}
