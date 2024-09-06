package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresDisciplineRepository implements DisciplineRepositoryPort {

    private final SpringDataDisciplineRepository springDataDisciplineRepository;

    public PostgresDisciplineRepository(SpringDataDisciplineRepository springDataDisciplineRepository) {
        this.springDataDisciplineRepository = springDataDisciplineRepository;
    }

    @Override
    public Discipline save(Discipline discipline) {
        return springDataDisciplineRepository.save(discipline);
    }
}
