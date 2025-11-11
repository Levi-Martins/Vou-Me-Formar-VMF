package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;
import com.smd.ufccursos.domain.DTO.response.SemesterElectiveStatus;
import com.smd.ufccursos.domain.entity.CourseRequirements;

import java.util.List;

public class GraduationCheckMapper {

    public static GraduationCheckResponse toResponse(
            boolean eligible,
            List<String> missingRequirements,
            List<String> missingMandatoryDisciplines,
            int totalMandatoryDisciplines,
            int completedMandatoryDisciplines,
            int completedMandatoryHours,
            CourseRequirements req,
            int completedOptionalHours,
            int completedComplementaryHours,
            boolean tccCompleted,
            Integer internshipCompleted,
            Integer extensionCompleted,
            List<SemesterElectiveStatus> semesterStatuses
    ) {
        int requiredMandatoryHours = safe(req.getRequiredMandatoryHours());
        int requiredOptionalHours = safe(req.getRequiredOptionalHours());
        int requiredComplementaryHours = safe(req.getRequiredComplementaryHours());
        int requiredTccHours = safe(req.getTccHours());
        int requiredInternshipHours = safe(req.getInternshipHours());
        int requiredExtensionHours = safe(req.getExtensionHours());

        int totalRequiredHours = requiredMandatoryHours
                + requiredOptionalHours
                + requiredComplementaryHours
                + requiredTccHours
                + requiredInternshipHours
                + requiredExtensionHours;

        int totalCompletedHours = completedMandatoryHours
                + completedOptionalHours
                + completedComplementaryHours
                + (Boolean.TRUE.equals(tccCompleted) ? requiredTccHours : 0)
                + (internshipCompleted != null && internshipCompleted > 0 ? requiredInternshipHours : 0)
                + (extensionCompleted != null && extensionCompleted > 0 ? requiredExtensionHours : 0);

        return GraduationCheckResponse.builder()
                .eligibleForGraduation(eligible)
                .missingRequirements(missingRequirements)
                .missingMandatoryDisciplines(missingMandatoryDisciplines)
                .totalMandatoryDisciplines(totalMandatoryDisciplines)
                .completedMandatoryDisciplines(completedMandatoryDisciplines)
                .totalOptionalHours(requiredOptionalHours)
                .completedOptionalHours(completedOptionalHours)
                .requiredComplementaryHours(requiredComplementaryHours)
                .completedComplementaryHours(completedComplementaryHours)
                .requiredExtensionHours(requiredExtensionHours)
                .completedExtensionHours(extensionCompleted != null && extensionCompleted > 0 ? requiredExtensionHours : 0)
                .requiredInternshipHours(requiredInternshipHours)
                .completedInternshipHours(internshipCompleted != null && internshipCompleted > 0 ? requiredInternshipHours : 0)
                .requiredTccHours(requiredTccHours)
                .completedTccHours(Boolean.TRUE.equals(tccCompleted) ? requiredTccHours : 0)
                .totalRequiredHours(totalRequiredHours)
                .totalCompletedHours(totalCompletedHours)
                .semesterElectiveStatuses(semesterStatuses)
                .build();
    }

    private static int safe(Integer value) {
        return value != null ? value : 0;
    }
}
