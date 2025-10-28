package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.ElectiveRequirementDTO;
import com.smd.ufccursos.domain.DTO.response.ElectiveRequirementDTOResponse;
import com.smd.ufccursos.domain.entity.SemesterElectiveRequirement;

public class SemesterElectiveRequirementMapper {

    public static SemesterElectiveRequirement toEntity(ElectiveRequirementDTO dto) {
        if (dto == null) return null;

        SemesterElectiveRequirement entity = new SemesterElectiveRequirement();
        entity.setSemester(dto.getSemester());
        entity.setMinEletivasRequired(dto.getMinRequired());
        return entity;
    }

    public static ElectiveRequirementDTOResponse toResponse(SemesterElectiveRequirement entity) {
        if (entity == null) return null;

        ElectiveRequirementDTOResponse dto = new ElectiveRequirementDTOResponse();
        dto.setSemester(entity.getSemester());
        dto.setMinEletivasRequired(entity.getMinEletivasRequired());
        return dto;
    }
}
