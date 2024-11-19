package com.smd.ufccursos.domain.ports.servicePort;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthorizationServicePort {
  UserDetails loadUserByUsername(String username);
}
