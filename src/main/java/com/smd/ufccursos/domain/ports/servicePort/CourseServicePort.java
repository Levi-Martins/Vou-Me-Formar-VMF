package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Course;

import java.util.UUID;

;

public interface CourseServicePort {
    PageTO<Course> findAll(PaginationTO paginationTO);
    Course findById(UUID id);
    Course save(Course course);
    Course update(UUID id, Course course);
    void deleteById(UUID id);
}
