package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.Discipline;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface DisciplineRepositoryPort extends BaseRepositoryPort<Discipline> {

    Optional<Discipline> findByDisciplineCode(String code);
    List<Discipline> findByCourseId(UUID courseId);
    List<Discipline> findByCourseIdAndDisciplineCodeIn(UUID courseId, Set<String> approvedCodes);
    List<Discipline> findByDisiplineCodeIn(Set<String> disiplineCodes);
    List<Discipline> findAllById(List<UUID> ids);
    List<Discipline> findByPrerequisiteId(UUID prerequisiteId);
}
