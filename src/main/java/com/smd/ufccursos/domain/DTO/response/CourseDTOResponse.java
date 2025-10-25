package com.smd.ufccursos.domain.DTO.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CourseDTOResponse {
    private UUID id;
    private String name;
    private String department;
    private CourseRequirementsDTOResponse requirements;
}
