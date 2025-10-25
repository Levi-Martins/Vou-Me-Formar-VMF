package com.smd.ufccursos.domain.DTO.response;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class ElectiveRequirementDTOResponse {
    private Integer semester;
    private Integer minEletivasRequired;
}
