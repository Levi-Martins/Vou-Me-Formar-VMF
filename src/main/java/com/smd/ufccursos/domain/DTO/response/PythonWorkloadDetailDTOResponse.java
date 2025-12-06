package com.smd.ufccursos.domain.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PythonWorkloadDetailDTOResponse {

    @JsonProperty("exigido")
    private String required; //exigido

    @JsonProperty("integralizado")
    private String completed; // integralizado

    @JsonProperty("computavel")
    private String computable; //computavel

    @JsonProperty("pendente")
    private String pending; //pendente

}
