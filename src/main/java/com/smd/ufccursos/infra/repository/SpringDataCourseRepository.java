package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataCourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {
}
