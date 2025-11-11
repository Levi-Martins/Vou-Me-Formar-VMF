package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.Discipline;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplineRepositoryPort extends BaseRepositoryPort<Discipline> {

    Optional<Discipline> findByDisciplineCode(String code);
    List<Discipline> findByCourseId(UUID courseId);
}
