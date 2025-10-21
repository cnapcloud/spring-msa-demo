package com.cop.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;

import com.cop.project.dto.ProjectDto;
import com.cop.project.entity.Project;
import com.cop.project.mapper.ProjectMapper;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
	    type = FilterType.ASSIGNABLE_TYPE, 
	    classes = {ProjectQueryDslRepository.class, ProjectRepository.class, ProjectMapper.class}))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableWebSecurity
@Slf4j
public class ProjectQueryDslRepositoryImplTest {
	
	@Autowired
	ProjectRepository projectRepository;
	
	   private Project project;
	   private Pageable pageable;

	    @BeforeEach
	    void setUp() {
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
	        
	        pageable = PageRequest.of(0, 5, Sort.by("name").descending());
	    }

		@Test
		@DisplayName("query project like name")
		void findByNameTest() {			
			projectRepository.save(project);
			Page<ProjectDto> result=projectRepository.findByNameUsingQuerydsl("Test", pageable);
			log.info(">>> result: {}", result.getContent());
	        assertEquals(1, result.getTotalElements());
	        assertEquals("Test Project", result.getContent().get(0).getName());
		}	
}
