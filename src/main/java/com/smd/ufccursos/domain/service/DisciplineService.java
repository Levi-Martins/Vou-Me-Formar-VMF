package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;

public class DisciplineService implements DisciplineServicePort {

    private final DisciplineRepositoryPort disciplineRepositoryPort;

    public DisciplineService(DisciplineRepositoryPort disciplineRepositoryPort) {
        this.disciplineRepositoryPort = disciplineRepositoryPort;
    }

    @Override
    public Discipline createDiscipline(Discipline discipline) {
        return disciplineRepositoryPort.save(discipline);
    }
}
