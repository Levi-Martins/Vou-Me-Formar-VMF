package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresCourseRepository implements CourseRepositoryPort {

    private final SpringDataCourseRepository springDataCourseRepository;

    public PostgresCourseRepository(SpringDataCourseRepository springDataCourseRepository) {
        this.springDataCourseRepository = springDataCourseRepository;
    }


    @Override
    public PageTO<Course> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Page<Course> page = springDataCourseRepository.findAll(pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
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
