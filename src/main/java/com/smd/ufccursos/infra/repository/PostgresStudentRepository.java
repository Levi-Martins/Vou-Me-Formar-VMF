package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageTO<Student> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Page<Student> page = springDataStudentRepository.findAll(pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public Student save(Student entity) {
        return springDataStudentRepository.save(entity);
    }

    @Override
    public Optional<Student> findById(UUID id) {
        return springDataStudentRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataStudentRepository.deleteById(id);
    }
}
