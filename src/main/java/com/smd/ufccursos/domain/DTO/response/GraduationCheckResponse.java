package com.smd.ufccursos.domain.DTO.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraduationCheckResponse {
    private boolean eligibleForGraduation;
    private List<String> missingRequirements;

    private List<String> missingMandatoryDisciplines;
    private List<SemesterElectiveStatus> semesterElectiveStatuses;


    private int totalMandatoryDisciplines;
    private int completedMandatoryDisciplines;

    private int totalOptionalHours;
    private int completedOptionalHours;

    private List<String> unknownDisciplines;

    private int requiredComplementaryHours;
    private int completedComplementaryHours;

    private int requiredExtensionHours;
    private int completedExtensionHours;

    private int requiredInternshipHours;
    private int completedInternshipHours;

    private int requiredTccHours;
    private int completedTccHours;

    private int totalRequiredHours;
    private int totalCompletedHours;
}
