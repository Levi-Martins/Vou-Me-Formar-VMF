package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.request.CourseDTORequest;
import com.smd.ufccursos.domain.entity.Course;

import java.util.UUID;

;

public interface CourseServicePort {
    PageTO<Course> findAll(PaginationTO paginationTO);
    Course findById(UUID id);
    Course save(CourseDTORequest courseDTORequest);
    Course update(UUID id, CourseDTORequest courseDTORequest);
    void deleteById(UUID id);
}
