package com.cop.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cop.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, String>, ProjectQueryDslRepository {


}
