package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.LoginResponseTO;
import com.smd.ufccursos.domain.DTO.request.AuthenticationTO;
import com.smd.ufccursos.domain.DTO.request.RegisterTO;
import com.smd.ufccursos.domain.entity.User;
import com.smd.ufccursos.domain.ports.repositoryPort.UserRepositoryPort;
import com.smd.ufccursos.domain.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserRepositoryPort userRepository;
  private final TokenService tokenService;

  public AuthenticationController(AuthenticationManager authenticationManager,
      UserRepositoryPort userRepository, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid AuthenticationTO data){
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
    var authentication = authenticationManager.authenticate(usernamePassword);
    var token = tokenService.generateToken((User) authentication.getPrincipal());
    return ResponseEntity.ok(new LoginResponseTO(token));
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody @Valid RegisterTO data){
    if (userRepository.findByLogin(data.login()).isPresent())
      return ResponseEntity.badRequest().build();

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    User newUser = new User(data.login(), encryptedPassword, data.name(), data.role());
    userRepository.save(newUser);
    return ResponseEntity.ok(newUser);
  }
}
