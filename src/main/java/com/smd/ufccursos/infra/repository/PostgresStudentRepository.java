package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresStudentRepository implements StudentRepositoryPort {

    private final SpringDataStudentRepository springDataStudentRepository;

    public PostgresStudentRepository(SpringDataStudentRepository springDataStudentRepository) {
        this.springDataStudentRepository = springDataStudentRepository;
    }

    @Override
    public Optional<Student> findById(UUID id) {
        return springDataStudentRepository.findById(id);
    }

    @Override
    public Student save(Student student) {
        return springDataStudentRepository.save(student);
    }
}
