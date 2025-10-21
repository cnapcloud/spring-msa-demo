package com.cop.project.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cop.project.dto.ProjectDto;
import com.cop.project.entity.Project;
import com.cop.project.mapper.ProjectMapper;
import com.cop.project.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final ProjectMapper projectMapper;

	public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
		this.projectRepository = projectRepository;
		this.projectMapper = projectMapper;
	}

	@Override
	public Page<ProjectDto> getAllProjects(Pageable pageable) {
		final Page<Project> page = projectRepository.findAll(pageable);

		return new PageImpl<>(page.getContent().stream()
				.map(projectMapper::toProjectDto).collect(Collectors.toList()),
				pageable, page.getTotalElements());
	}

	@Override
	public Page<ProjectDto> getProjectByName(String name, Pageable pageable) {
		return projectRepository.findByNameUsingQuerydsl(name, pageable);
	}

	@Override
	public ProjectDto getProjectById(String id) {
		Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
		return projectMapper.toProjectDto(project);
	}

	@Override
	public ProjectDto createProject(ProjectDto projectDto) {
		Project project = projectMapper.toProjectEntity(projectDto);
		project.setEnabled(true);
		project = projectRepository.save(project);
		return projectMapper.toProjectDto(project);
	}

	@Override
	public ProjectDto updateProject(String id, ProjectDto projectDto) {
		Project project = projectMapper.toProjectEntity(projectDto);
		project.setId(id);
		project = projectRepository.save(project);
		return projectMapper.toProjectDto(project);
	}

	@Override
	public void deleteProject(String id) {
		projectRepository.deleteById(id);
	}
}
