package com.smd.ufccursos.domain.DTO.request;

import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineDTORequest {
    @NotBlank(message = "O nome da disciplina não pode ser vazio ou nulo.")
    private String name;

    @NotNull(message = "O tipo de disciplina não pode ser nulo")
    private TypeOfDiscipline typeOfDiscipline;

    @NotNull(message = "A carga horária da disciplina não pode ser nula")
    @Min(value = 1, message = "A carga horária da disciplina deve ser maior que 0.")
    private Integer workload;

    @NotNull(message = "A quantidade de crétidos da disciplina não pode ser nula")
    @Min(value = 1, message = "A quantidade de crétidos da disciplina deve ser maior que 0.")
    private Integer classCredits;

    private String description;

    @NotNull(message = "O semestre não pode ser nulo.")
    @Min(value = 1, message = "O semestre deve ser maior que 0.")
    private Integer semester;

    @NotNull(message = "O curso não pode ser nulo.")
    private UUID courseId;

    private Set<UUID> prerequisiteIds;

    @NotBlank(message = "O código da disciplina não pode ser vazio ou nulo.")
    private String disciplineCode;


}
