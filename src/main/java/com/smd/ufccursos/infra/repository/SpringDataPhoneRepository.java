package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPhoneRepository extends JpaRepository<Phone, UUID> {
}
