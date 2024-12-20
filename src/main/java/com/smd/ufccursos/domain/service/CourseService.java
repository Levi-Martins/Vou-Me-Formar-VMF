package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.request.CourseDTORequest;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.exceptions.ObjectNotFoundException;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;

import java.util.Optional;
import java.util.UUID;

public class CourseService implements CourseServicePort {

    private final CourseRepositoryPort courseRepositoryPort;

    public CourseService(CourseRepositoryPort courseRepositoryPort) {
        this.courseRepositoryPort = courseRepositoryPort;
    }


    @Override
    public PageTO<Course> findAll(PaginationTO paginationTO) {
        return courseRepositoryPort.findAll(paginationTO);
    }

    @Override
    public Course findById(UUID id) {
        Optional<Course> course = courseRepositoryPort.findById(id);
        if (course.isEmpty()) {
            throw new ObjectNotFoundException("Course not found ");
        }
        return course.get();
    }

    @Override
    public Course save(CourseDTORequest courseDTORequest) {
        Course course = Course.builder()
                .name(courseDTORequest.getName())
                .department(courseDTORequest.getDepartment())
                .build();
        return courseRepositoryPort.save(course);
    }

    @Override
    public Course update(UUID id, CourseDTORequest courseDTORequest) {
        Course courseToUpdate = findById(id);
        courseToUpdate.setName(courseDTORequest.getName());
        courseToUpdate.setDepartment(courseDTORequest.getDepartment());

        return courseRepositoryPort.save(courseToUpdate);
    }

    @Override
    public void deleteById(UUID id) {
        courseRepositoryPort.deleteById(id);
    }
}
