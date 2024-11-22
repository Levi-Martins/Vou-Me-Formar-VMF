package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.Discipline;

import java.util.Optional;

public interface DisciplineRepositoryPort extends BaseRepositoryPort<Discipline> {

    Optional<Discipline> findByDisciplineCode(String code);
}
