package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDisciplineRepository extends JpaRepository<Discipline, UUID> {
}
