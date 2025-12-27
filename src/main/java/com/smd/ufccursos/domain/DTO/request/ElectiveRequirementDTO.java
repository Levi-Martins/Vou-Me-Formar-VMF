package com.smd.ufccursos.domain.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElectiveRequirementDTO {
    private Integer semester;
    @JsonProperty("minEletivasRequired")
    private Integer minRequired;
}
