package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresCourseRepository implements CourseRepositoryPort {

    private final SpringDataCourseRepository springDataCourseRepository;

    public PostgresCourseRepository(SpringDataCourseRepository springDataCourseRepository) {
        this.springDataCourseRepository = springDataCourseRepository;
    }

    @Override
    public Course save(Course course) {
        return springDataCourseRepository.save(course);
    }
}
