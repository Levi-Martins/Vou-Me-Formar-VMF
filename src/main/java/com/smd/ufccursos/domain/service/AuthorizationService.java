package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.ports.repositoryPort.UserRepositoryPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthorizationService implements UserDetailsService {

  private final UserRepositoryPort userRepository;

  public AuthorizationService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByLogin(username).get();
  }
}
