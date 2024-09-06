package com.smd.ufccursos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Discipline extends BaseEntity{

    private String name;

    private TypeOfDiscipline typeOfDiscipline;

    private Integer workload;

    private Integer classCredits;

    private String description;

    private Integer semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "discipline",cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Prerequisite> prerequisites;


}
