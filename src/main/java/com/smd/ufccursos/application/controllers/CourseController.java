package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class CourseController {

    private final CourseServicePort courseServicePort;

    public CourseController(CourseServicePort courseServicePort) {
        this.courseServicePort = courseServicePort;
    }

    @PostMapping
    public Course create(@RequestBody Course course){
        return  courseServicePort.save(course);
    }
}
