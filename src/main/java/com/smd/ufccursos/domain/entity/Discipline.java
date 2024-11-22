package com.smd.ufccursos.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Discipline extends BaseEntity{

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeOfDiscipline typeOfDiscipline;

    @NotNull
    private Integer workload;

    @NotNull
    private Integer classCredits;

    private String description;

    private Integer semester;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToMany
    @JoinTable(
            name = "discipline_prerequisites",
            joinColumns = @JoinColumn(name = "discipline_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private Set<Discipline> prerequisites;

    @NotNull
    private String disciplineCode;


}
