package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataStudentRepository extends JpaRepository<Student, UUID> {
}
