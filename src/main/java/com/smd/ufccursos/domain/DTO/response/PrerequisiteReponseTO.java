package com.smd.ufccursos.domain.DTO.response;

import com.smd.ufccursos.domain.entity.Discipline;
import lombok.Data;

import java.util.UUID;

@Data
public class PrerequisiteReponseTO {
    private UUID id;
    private String name;
    private String disciplineCode;

    public PrerequisiteReponseTO(Discipline discipline){
        this.id = discipline.getId();
        this.name = discipline.getName();
        this.disciplineCode = discipline.getDisciplineCode();
    }
}


