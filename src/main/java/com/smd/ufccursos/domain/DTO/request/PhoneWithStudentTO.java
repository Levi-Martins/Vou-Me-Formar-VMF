package com.smd.ufccursos.domain.DTO.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class PhoneWithStudentTO extends PhoneTO {
    private UUID studentId;
}