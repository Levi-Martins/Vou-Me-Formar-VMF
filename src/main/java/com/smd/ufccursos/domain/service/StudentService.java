package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;

import java.util.Optional;
import java.util.UUID;

public class StudentService implements StudentServicePort {

    private final StudentRepositoryPort studentRepositoryPort;

    public StudentService(StudentRepositoryPort studentRepositoryPort) {
        this.studentRepositoryPort = studentRepositoryPort;
    }

    @Override
    public Optional<Student> findStudentById(UUID id) {
        return studentRepositoryPort.findById(id);
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepositoryPort.save(student);
    }
}
