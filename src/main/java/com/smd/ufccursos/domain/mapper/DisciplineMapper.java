package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.DisciplineDTORequest;
import com.smd.ufccursos.domain.DTO.response.DisciplineResponseDTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.DTO.response.PrerequisiteReponseTO;

import java.util.stream.Collectors;

public class DisciplineMapper {
    public static Discipline toEntity(DisciplineDTORequest dto) {
        if (dto == null) return null;

        Discipline discipline = new Discipline();
        discipline.setName(dto.getName());
        discipline.setTypeOfDiscipline(dto.getTypeOfDiscipline());
        discipline.setWorkload(dto.getWorkload());
        discipline.setClassCredits(dto.getClassCredits());
        discipline.setDescription(dto.getDescription());
        discipline.setSemester(dto.getSemester());
        discipline.setDisciplineCode(dto.getDisciplineCode());

        return discipline;
    }

    public static DisciplineResponseDTO toResponse(Discipline entity) {
        if (entity == null) return null;

        DisciplineResponseDTO dto = new DisciplineResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTypeOfDiscipline(entity.getTypeOfDiscipline() != null ? entity.getTypeOfDiscipline().name() : null);
        dto.setWorkload(entity.getWorkload());
        dto.setClassCredits(entity.getClassCredits());
        dto.setDescription(entity.getDescription());
        dto.setSemester(entity.getSemester());
        dto.setDisciplineCode(entity.getDisciplineCode());

        if (entity.getCourse() != null) {
            dto.setCourseId(entity.getCourse().getId());
            dto.setCourseName(entity.getCourse().getName());
        }

        if (entity.getPrerequisites() != null) {
            dto.setPrerequisites(entity.getPrerequisites().stream()
                    .map(PrerequisiteReponseTO::new)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
