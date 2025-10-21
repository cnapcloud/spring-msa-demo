package com.cop.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.cop.project.dto.ProjectDto;
import com.cop.project.entity.Project;
import com.cop.project.mapper.ProjectMapper;
import com.cop.project.repository.ProjectRepository;
import com.cop.project.service.ProjectServiceImpl;

public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private ProjectDto projectDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setId(UUID.randomUUID().toString());
        project.setName("Test Project");
        project.setRegion("us-east-1");
        project.setAccessKey("access-key-123");
        project.setSecretKey("******");
        project.setEnabled(true);
        project.setCreatedBy("123");
        project.setCreatedDate(LocalDateTime.of(2024, 12, 29, 9, 59, 13));
        project.setLastModifiedBy("123");
        project.setLastModifiedDate(LocalDateTime.of(2024, 12, 29, 9, 59, 13));       
        
        projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName("Test Project");
        project.setRegion("us-east-1");
        projectDto.setAccessKey("access-key-123");
        projectDto.setSecretKey("******");
        projectDto.setEnabled(true);
        projectDto.setCreatedBy("123");
        projectDto.setCreatedDate(LocalDateTime.of(2024, 12, 29, 9, 59, 13));
        projectDto.setLastModifiedBy("123");
        projectDto.setLastModifiedDate(LocalDateTime.of(2024, 12, 29, 9, 59, 13));
               
        pageable = PageRequest.of(0, 5, Sort.by("name").descending());
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(Stream.of(project).collect(Collectors.toList()), pageable, 1));
        when(projectMapper.toProjectDto(any(Project.class))).thenReturn(projectDto);

        Page<ProjectDto> result = projectService.getAllProjects(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Project", result.getContent().get(0).getName());
    }

    @Test
    void getProjectByName() {
        when(projectRepository.findByNameUsingQuerydsl("Test Project", pageable))
            .thenReturn(new PageImpl<>(Stream.of(projectDto).collect(Collectors.toList()), pageable, 1));

        Page<ProjectDto> result = projectService.getProjectByName("Test Project", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Project", result.getContent().get(0).getName());
    }

    @Test
    void getProjectById() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectMapper.toProjectDto(any(Project.class))).thenReturn(projectDto);

        ProjectDto result = projectService.getProjectById(project.getId());

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
    }

    @Test
    void getProjectById_NotFound() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.getProjectById(project.getId());
        });

        String expectedMessage = "Project not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createProject() {
        when(projectMapper.toProjectEntity(any(ProjectDto.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toProjectDto(any(Project.class))).thenReturn(projectDto);

        ProjectDto result = projectService.createProject(projectDto);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
    }

    @Test
    void updateProject() {
        when(projectMapper.toProjectEntity(any(ProjectDto.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toProjectDto(any(Project.class))).thenReturn(projectDto);

        ProjectDto result = projectService.updateProject(project.getId(), projectDto);

        assertNotNull(result);
        assertEquals(project.getId(), result.getId());
        assertEquals("Test Project", result.getName());
    }

    @Test
    void deleteProject() {
        projectService.deleteProject(project.getId());

        Mockito.verify(projectRepository, Mockito.times(1)).deleteById(project.getId());
    }
}
