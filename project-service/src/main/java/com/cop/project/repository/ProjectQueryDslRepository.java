package com.cop.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cop.project.dto.ProjectDto;

public interface ProjectQueryDslRepository {
	
	public Page<ProjectDto> findByNameUsingQuerydsl(String name, Pageable pageable);
}
