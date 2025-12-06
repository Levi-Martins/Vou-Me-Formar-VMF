package com.smd.ufccursos.domain.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PythonWorkLoadSumaryDTOResponse {

    @JsonProperty("Carga Horária Total")
    private PythonWorkloadDetailDTOResponse totalWorkload;

    @JsonProperty("Carga Horária Optativa")
    private PythonWorkloadDetailDTOResponse electiveWorkload;

    @JsonProperty("Carga Horária de Atividades Complementares")
    private PythonWorkloadDetailDTOResponse complementaryActivities;

    @JsonProperty("Carga Horária de Componentes Optativos Livres")
    private PythonWorkloadDetailDTOResponse freeElectives;

    @JsonProperty("Carga Horária de TCC")
    private PythonWorkloadDetailDTOResponse finalProject;

    @JsonProperty("Carga Horária de Estágio")
    private PythonWorkloadDetailDTOResponse internship;

    @JsonProperty("Carga Horária de Extensão")
    private PythonWorkloadDetailDTOResponse extension;
}
