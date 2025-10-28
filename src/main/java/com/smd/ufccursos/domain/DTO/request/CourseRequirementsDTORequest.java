package com.smd.ufccursos.domain.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CourseRequirementsDTORequest {

    @NotNull(message = "O total de horas obrigatórias não pode ser nulo.")
    @Min(value = 0, message = "O total de horas obrigatórias deve ser 0 ou maior.")
    private Integer requiredMandatoryHours;

    @NotNull(message = "O total de horas optativas não pode ser nulo.")
    @Min(value = 0, message = "O total de horas optativas deve ser 0 ou maior.")
    private Integer requiredOptionalHours;

    @NotNull(message = "O total de horas complementares não pode ser nulo.")
    @Min(value = 0, message = "O total de horas complementares deve ser 0 ou maior.")
    private Integer requiredComplementaryHours;

    private Integer tccHours;
    private Integer internshipHours;
    private Integer extensionHours;
    private List<ElectiveRequirementDTO> electiveRequirements;
}
