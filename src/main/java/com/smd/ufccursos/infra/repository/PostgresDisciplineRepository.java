package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresDisciplineRepository implements DisciplineRepositoryPort {

    private final SpringDataDisciplineRepository springDataDisciplineRepository;

    public PostgresDisciplineRepository(SpringDataDisciplineRepository springDataDisciplineRepository) {
        this.springDataDisciplineRepository = springDataDisciplineRepository;
    }

    @Override
    public PageTO<Discipline> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Page<Discipline> page = springDataDisciplineRepository.findAll(pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public Discipline save(Discipline discipline) {
        return springDataDisciplineRepository.save(discipline);
    }

    @Override
    public Optional<Discipline> findById(UUID id) {
        return springDataDisciplineRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataDisciplineRepository.deleteById(id);
    }
}
