package com.cop.project.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cop.project.dto.ProjectDto;
import com.cop.project.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v2/project")
@Slf4j
@Tag(name = "Project API V2", description = "API for managing cloud project")
public class ProjectControllerV2 {

    private final ProjectService projectService;

    public ProjectControllerV2(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    public Page<ProjectDto> getAllProjects(
            @Parameter(description = "Pagination information", example = "{ \"page\": 0, \"size\": 1, \"sort\": [\"name,desc\"] }") @PageableDefault(sort = "name", direction = Sort.Direction.DESC, size = 5) final Pageable pageable) {
        log.info("get all projects");
        return projectService.getAllProjects(pageable);
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a project by its ID")
    public ProjectDto getProjectById(@PathVariable String id) {
        log.info("get project: {}", id);
        return projectService.getProjectById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Get project by name", description = "Retrieve projects by name")
    public Page<ProjectDto> getProjectByName(@RequestParam(required = false) String name,
            @Parameter(description = "Pagination information", example = "{ \"page\": 0, \"size\": 1, \"sort\": [\"name,desc\"] }") Pageable pageable) {
        return projectService.getProjectByName(name, pageable);
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping
    @Operation(summary = "Create a new project", description = "Create a new project with the provided details")
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        log.info("post project: {}", projectDto);
        return projectService.createProject(projectDto);
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Update an existing project with the provided details")
    public ProjectDto updateProject(@PathVariable String id, @RequestBody ProjectDto projectDto) {
        return projectService.updateProject(id, projectDto);
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Delete a project by its ID")
    public void deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
    }
}
