package com.smd.ufccursos.domain.DTO.response;

import com.smd.ufccursos.domain.entity.Discipline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineResponseDTO {
    private UUID id;
    private String name;
    private String typeOfDiscipline;
    private Integer workload;
    private Integer classCredits;
    private String description;
    private Integer semester;
    private UUID courseId;
    private String courseName;
    private List<PrerequisiteReponseTO> prerequisites;
    private String disciplineCode;

    public DisciplineResponseDTO(Discipline discipline) {
        this.id = discipline.getId();
        this.name = discipline.getName();
        this.typeOfDiscipline = discipline.getTypeOfDiscipline().toString();
        this.workload = discipline.getWorkload();
        this.classCredits = discipline.getClassCredits();
        this.description = discipline.getDescription();
        this.semester = discipline.getSemester();
        this.courseId = discipline.getCourse() != null ? discipline.getCourse().getId() : null;
        this.courseName = discipline.getCourse() != null ? discipline.getCourse().getName() : null;        this.prerequisites = discipline.getPrerequisites() != null
                ? discipline.getPrerequisites().stream()
                .map(PrerequisiteReponseTO::new)
                .collect(Collectors.toList())
                : List.of();
        this.disciplineCode = discipline.getDisciplineCode();
    }
}
