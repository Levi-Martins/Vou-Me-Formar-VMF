package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataCourseRepository extends JpaRepository<Course, UUID> {
}
