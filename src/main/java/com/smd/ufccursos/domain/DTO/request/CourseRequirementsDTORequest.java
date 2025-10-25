package com.smd.ufccursos.domain.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseRequirementsDTORequest {
    private Integer requiredMandatoryHours;
    private Integer requiredOptionalHours;
    private Integer requiredComplementaryHours;
    private Integer tccHours;
    private Integer internshipHours;
    private Integer extensionHours;
    private List<ElectiveRequirementDTO> electiveRequirements;
}
