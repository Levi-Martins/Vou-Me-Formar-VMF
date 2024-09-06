package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.entity.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentServicePort {

    Optional<Student> findStudentById(UUID id);
    Student createStudent(Student student);
}
