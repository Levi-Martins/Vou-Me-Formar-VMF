package com.smd.ufccursos.domain.DTO;

import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineTO {
    private String name;

    private TypeOfDiscipline typeOfDiscipline;

    private Integer workload;

    private Integer classCredits;

    private String description;

    private Integer semester;

    private UUID courseId;

}