package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.Discipline;

public interface DisciplineRepositoryPort {

    Discipline save(Discipline discipline);
}
