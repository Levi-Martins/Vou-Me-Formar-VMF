package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.Course;

public interface CourseRepositoryPort {

    Course save(Course course);
}
