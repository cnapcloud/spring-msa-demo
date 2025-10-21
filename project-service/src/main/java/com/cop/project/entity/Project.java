package com.cop.project.entity;

import com.cop.common.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "project")
public class Project  extends Auditable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "access_key", nullable = false)
    private String accessKey;

    @Column(name = "secret_key", nullable = false)
    private String secretKey;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled=true;
}
