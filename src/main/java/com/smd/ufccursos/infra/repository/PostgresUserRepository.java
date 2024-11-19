package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.entity.User;
import com.smd.ufccursos.domain.ports.repositoryPort.UserRepositoryPort;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresUserRepository implements UserRepositoryPort {

  private final SpringDataUserRepository userRepository;

  public PostgresUserRepository(SpringDataUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<UserDetails> findByLogin(String login) {
    return userRepository.findByLogin(login);
  }

  @Override
  public User save(User entity) {
    return userRepository.save(entity);
  }
}
