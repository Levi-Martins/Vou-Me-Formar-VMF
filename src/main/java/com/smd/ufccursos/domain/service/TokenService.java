package com.smd.ufccursos.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.smd.ufccursos.domain.entity.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;

public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256("secret");
      String token = JWT.create()
          .withIssuer("VMF-API")
          .withSubject(user.getLogin())
          .withExpiresAt(genExpirationDate())
          .sign(algorithm);
      return token;
    } catch (JWTCreationException e) {
      throw new RuntimeException("Error while generating token ", e);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256("secret");
      return JWT.require(algorithm)
          .withIssuer("VMF-API")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException e) {
      return e.getMessage();
    }
  }

  private Instant genExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }

}
