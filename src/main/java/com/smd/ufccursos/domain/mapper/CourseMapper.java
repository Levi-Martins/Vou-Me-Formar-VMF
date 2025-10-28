package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.CourseDTORequest;
import com.smd.ufccursos.domain.DTO.response.CourseDTOResponse;
import com.smd.ufccursos.domain.entity.Course;

public class CourseMapper {
    public static Course toEntity(CourseDTORequest dto) {
        if (dto == null) return null;

        return Course.builder()
                .name(dto.getName())
                .department(dto.getDepartment())
                .requirements(CourseRequirementsMapper.toEntity(dto.getRequirements()))
                .build();
    }

    public static CourseDTOResponse toResponse(Course entity) {
        if (entity == null) return null;

        CourseDTOResponse dto = new CourseDTOResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDepartment(entity.getDepartment());
        dto.setRequirements(CourseRequirementsMapper.toResponse(entity.getRequirements()));

        return dto;
    }
}
