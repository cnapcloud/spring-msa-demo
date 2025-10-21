package com.cop.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cop.project.dto.ProjectDto;
import com.cop.project.service.ProjectService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class ProjectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProjectService projectService;

	@Test
	public void testGetProjectById() throws Exception {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setId("1");
		projectDto.setName("Sample Project");

		when(projectService.getProjectById("1")).thenReturn(projectDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/project/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value("1")).andExpect(jsonPath("$.name").value("Sample Project"));
	}

	@Test
	public void testCreateProject() throws Exception {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setId("1");
		projectDto.setName("New Project");

		when(projectService.createProject(any(ProjectDto.class))).thenReturn(projectDto);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/project").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"New Project\"}")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.name").value("New Project"));
	}
}
