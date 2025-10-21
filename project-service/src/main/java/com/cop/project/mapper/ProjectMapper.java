package com.cop.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cop.project.dto.ProjectDto;
import com.cop.project.entity.Project;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDto toProjectDto(Project project);

    @Mapping(target = "enabled", ignore = true)
    Project toProjectEntity(ProjectDto projectDto);
    
}
