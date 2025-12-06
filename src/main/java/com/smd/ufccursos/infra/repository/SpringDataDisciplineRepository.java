package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SpringDataDisciplineRepository extends JpaRepository<Discipline, UUID>, JpaSpecificationExecutor<Discipline> {
    Optional<Discipline> findByDisciplineCode(String code);
    List<Discipline> findByCourseId(UUID courseId);
    List<Discipline> findByCourse_IdAndDisciplineCodeIn(UUID courseId, Set<String> disciplineCodes);
}
