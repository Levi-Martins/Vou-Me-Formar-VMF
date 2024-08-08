package com.smd.ufccursos.domain.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
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
