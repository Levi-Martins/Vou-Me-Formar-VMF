package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepositoryPort {

    Optional<Student> findById(UUID student);
    Student save(Student student);
}
