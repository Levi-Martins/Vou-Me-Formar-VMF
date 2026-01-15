package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.LoginResponseTO;
import com.smd.ufccursos.domain.DTO.request.AuthenticationTO;
import com.smd.ufccursos.domain.entity.User;
import com.smd.ufccursos.domain.ports.repositoryPort.UserRepositoryPort;
import com.smd.ufccursos.domain.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("[CT-04] - Deve retornar token e status 200 quando credenciais forem válidas")
    void shouldLoginSuccessfully_WhenCredentialsAreValid() {
        // 1. ARRANGE
        String loginValido = "admin@email.com";
        String senhaValida = "senha123";
        String tokenGerado = "token_jwt_valido_mockado";

        AuthenticationTO dadosLogin = new AuthenticationTO(loginValido, senhaValida);

        User usuarioAutenticado = User.builder()
                .login(loginValido)
                .name("Administrador")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(usuarioAutenticado);

        when(tokenService.generateToken(usuarioAutenticado)).thenReturn(tokenGerado);

        // 2. ACT
        ResponseEntity<Object> response = authenticationController.login(dadosLogin);

        // 3. ASSERT
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verifica se é 200 OK

        assertTrue(response.getBody() instanceof LoginResponseTO);

        LoginResponseTO responseBody = (LoginResponseTO) response.getBody();
        assertEquals(tokenGerado, responseBody.token());

        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("[CT-05] - Deve lançar exceção (BadCredentials) quando email ou senha forem inválidos")
    void shouldThrowException_WhenCredentialsAreInvalid() {
        // 1. ARRANGE
        String loginInvalido = "naoexiste@email.com";
        String senhaQualquer = "123456";

        AuthenticationTO dadosLogin = new AuthenticationTO(loginInvalido, senhaQualquer);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // 2. ACT & ASSERT
        assertThrows(BadCredentialsException.class, () -> {
            authenticationController.login(dadosLogin);
        });

        verify(tokenService, never()).generateToken(any());
    }
}