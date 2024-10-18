package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class PostgresCourseRepository implements CourseRepositoryPort {

    private final SpringDataCourseRepository springDataCourseRepository;

    public PostgresCourseRepository(SpringDataCourseRepository springDataCourseRepository) {
        this.springDataCourseRepository = springDataCourseRepository;
    }


    @Override
    public PageTO<Course> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Specification<Course> spec = getFilteredCourses(paginationTO.getParams());
        Page<Course> page = springDataCourseRepository.findAll(spec, pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    private Specification<Course> getFilteredCourses(Map<String, Object> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("name") && params.get("name") != null) {
                String name = ((String) params.get("name")).toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name + "%"));
            }

            if (params.containsKey("department") && params.get("department") != null) {
                String department = ((String) params.get("department")).toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("department")), "%" + department + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Course save(Course entity) {
        return springDataCourseRepository.save(entity);
    }

    @Override
    public Optional<Course> findById(UUID id) {
        return springDataCourseRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataCourseRepository.deleteById(id);
    }
}
