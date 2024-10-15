package com.smd.ufccursos.domain.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDTORequest {
    @NotBlank
    private String name;

    private String department;
}
