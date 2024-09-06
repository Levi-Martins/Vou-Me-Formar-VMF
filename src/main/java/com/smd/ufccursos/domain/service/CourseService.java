package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;

public class CourseService implements CourseServicePort {

    private final CourseRepositoryPort courseRepositoryPort;

    public CourseService(CourseRepositoryPort courseRepositoryPort) {
        this.courseRepositoryPort = courseRepositoryPort;
    }

    @Override
    public Course save(Course course) {
        return courseRepositoryPort.save(course);
    }
}
