package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.entity.Course;

public interface CourseServicePort {
    Course save(Course course);
}
