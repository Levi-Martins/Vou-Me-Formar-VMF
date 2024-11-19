package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {
  Optional<UserDetails> findByLogin(String login);
}
