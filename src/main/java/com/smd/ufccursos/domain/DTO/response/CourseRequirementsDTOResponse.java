package com.smd.ufccursos.domain.DTO.response;

import lombok.Data;

import java.util.List;

@Data
public class CourseRequirementsDTOResponse {
    private Integer requiredMandatoryHours;
    private Integer requiredOptionalHours;
    private Integer requiredComplementaryHours;
    private Integer tccHours;
    private Integer internshipHours;
    private Integer extensionHours;
    private List<ElectiveRequirementDTOResponse> semesterElectiveRequirementList;
}
