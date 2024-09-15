package com.smd.ufccursos.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Discipline extends BaseEntity{

    private String name;

    private TypeOfDiscipline typeOfDiscipline;

    private Integer workload;

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


}
