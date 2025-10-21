package com.cop.project.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import com.cop.project.dto.ProjectDto;
import com.cop.project.entity.Project;
import com.cop.project.entity.QProject;
import com.cop.project.mapper.ProjectMapper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ProjectQueryDslRepositoryImpl implements ProjectQueryDslRepository {
    private final EntityManager entityManager;
    private final ProjectMapper projectMapper;

    @Override
    public Page<ProjectDto> findByNameUsingQuerydsl(String name, Pageable pageable) {
        JPAQuery<Project> query = createQuery(name, pageable);
 
        var results = query.fetch();
        JPAQuery<Long> totalCount = createCountQuery(name);
        List<ProjectDto> content = results.stream().map(projectMapper::toProjectDto).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }


    protected JPAQuery<Project> createQuery(String name, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProject project = QProject.project;
        
        BooleanExpression condition = (name != null && !name.isBlank())
                ? project.name.contains(name)
                : null;

        JPAQuery<Project> query = queryFactory.selectFrom(project)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = new PathBuilder<>(Object.class, order.getProperty());
			query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name()), path));
        }

        return query;
    }


    protected JPAQuery<Long> createCountQuery(String name) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProject project = QProject.project;
        
        BooleanExpression condition = (name != null && !name.isBlank())
                ? project.name.contains(name)
                : null;

        return queryFactory.selectFrom(project).select(Wildcard.count).where(condition);
    }
}
