package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentServicePort studentServicePort;

    public StudentController(StudentServicePort studentServicePort) {
        this.studentServicePort = studentServicePort;
    }

    @PostMapping
    public Student create(@RequestBody Student student){
        return  studentServicePort.createStudent(student);
    }
}
