package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.entity.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepositoryPort {
  Optional<UserDetails> findByLogin(String login);
  User save(User entity);

}
