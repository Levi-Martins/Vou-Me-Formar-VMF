package com.smd.ufccursos.domain.DTO.request;

import com.smd.ufccursos.domain.entity.UserRole;

public record RegisterTO(String login, String password, String name, UserRole role) {
}
