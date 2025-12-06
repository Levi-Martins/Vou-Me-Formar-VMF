package com.smd.ufccursos.domain.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PythonPdfResponseDTO {

    @JsonProperty("disciplinas")
    private List<PythonDiscipleneDTOResponse> disciplines;

    @JsonProperty("resumo_carga_horaria")
    private PythonWorkLoadSumaryDTOResponse workloadSummary;

}
