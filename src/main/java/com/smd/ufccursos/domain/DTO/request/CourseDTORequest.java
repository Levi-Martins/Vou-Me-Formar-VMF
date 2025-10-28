package com.smd.ufccursos.domain.DTO.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDTORequest {
    @NotBlank(message = "O nome do curso não pode ser vazio ou nulo.")
    private String name;

    private String department;

    @Valid
    private CourseRequirementsDTORequest requirements;

}
