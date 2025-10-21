package com.cop.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cop.project.dto.ProjectDto;

public interface ProjectService {

	Page<ProjectDto> getAllProjects(Pageable pageable);

	Page<ProjectDto> getProjectByName(String name, Pageable pageable);

	ProjectDto getProjectById(String id);

	ProjectDto createProject(ProjectDto projectDto);

	ProjectDto updateProject(String id, ProjectDto projectDto);

	void deleteProject(String id);

}