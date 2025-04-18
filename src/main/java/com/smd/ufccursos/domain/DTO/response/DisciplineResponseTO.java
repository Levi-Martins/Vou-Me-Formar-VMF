package com.smd.ufccursos.domain.DTO.response;

import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.entity.Discipline;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class DisciplineResponseTO {
    private UUID id;
    private String name;
    private String typeOfDiscipline;
    private Integer workload;
    private Integer classCredits;
    private String description;
    private Integer semester;
    private Course course;
    private List<PrerequisiteReponseTO> prerequisites;
    private String disciplineCode;

    public DisciplineResponseTO(Discipline discipline) {
        this.id = discipline.getId();
        this.name = discipline.getName();
        this.typeOfDiscipline = discipline.getTypeOfDiscipline().toString();
        this.workload = discipline.getWorkload();
        this.classCredits = discipline.getClassCredits();
        this.description = discipline.getDescription();
        this.semester = discipline.getSemester();
        this.course = discipline.getCourse();
        this.prerequisites = discipline.getPrerequisites() != null
                ? discipline.getPrerequisites().stream()
                .map(PrerequisiteReponseTO::new)
                .collect(Collectors.toList())
                : List.of();
        this.disciplineCode = discipline.getDisciplineCode();
    }
}
