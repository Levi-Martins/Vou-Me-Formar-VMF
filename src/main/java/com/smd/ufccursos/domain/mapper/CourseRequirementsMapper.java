package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.CourseRequirementsDTORequest;
import com.smd.ufccursos.domain.DTO.response.CourseRequirementsDTOResponse;
import com.smd.ufccursos.domain.entity.CourseRequirements;
import com.smd.ufccursos.domain.entity.SemesterElectiveRequirement;

import java.util.List;
import java.util.stream.Collectors;

public class CourseRequirementsMapper {
    public static CourseRequirements toEntity(CourseRequirementsDTORequest dto) {
        if (dto == null) return null;

        CourseRequirements requirements = CourseRequirements.builder()
                .requiredMandatoryHours(dto.getRequiredMandatoryHours())
                .requiredOptionalHours(dto.getRequiredOptionalHours())
                .requiredComplementaryHours(dto.getRequiredComplementaryHours())
                .tccHours(dto.getTccHours())
                .internshipHours(dto.getInternshipHours())
                .extensionHours(dto.getExtensionHours())
                .build();

        if (dto.getElectiveRequirements() != null) {
            List<SemesterElectiveRequirement> electiveList = dto.getElectiveRequirements()
                    .stream()
                    .map(er -> {
                        SemesterElectiveRequirement entity =
                                SemesterElectiveRequirementMapper.toEntity(er);
                        entity.setCourseRequirements(requirements);
                        return entity;
                    })
                    .collect(Collectors.toList());
            requirements.setSemesterElectiveRequirementList(electiveList);
        }

        return requirements;
    }

    public static CourseRequirementsDTOResponse toResponse(CourseRequirements entity) {
        if (entity == null) return null;

        CourseRequirementsDTOResponse dto = new CourseRequirementsDTOResponse();
        dto.setRequiredMandatoryHours(entity.getRequiredMandatoryHours());
        dto.setRequiredOptionalHours(entity.getRequiredOptionalHours());
        dto.setRequiredComplementaryHours(entity.getRequiredComplementaryHours());
        dto.setTccHours(entity.getTccHours());
        dto.setInternshipHours(entity.getInternshipHours());
        dto.setExtensionHours(entity.getExtensionHours());

        if (entity.getSemesterElectiveRequirementList() != null) {
            dto.setSemesterElectiveRequirementList(
                    entity.getSemesterElectiveRequirementList().stream()
                            .map(SemesterElectiveRequirementMapper::toResponse)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
