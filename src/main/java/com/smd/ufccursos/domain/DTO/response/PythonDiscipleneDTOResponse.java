package com.smd.ufccursos.domain.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PythonDiscipleneDTOResponse {

    @JsonProperty("periodo")
    private String semester;

    @JsonProperty("codigo")
    private String disciplineCode;

    @JsonProperty("nome")
    private String disciplineName;

    @JsonProperty("ch")
    private String workload; //ch

    @JsonProperty("turma")
    private String disciplineClass; //turma

    @JsonProperty("frequencia")
    private String frequency;

    @JsonProperty("nota")
    private String grade; //nota

    @JsonProperty("situacao")
    private String situation;
}
