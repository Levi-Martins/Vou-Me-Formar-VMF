package com.smd.ufccursos.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SemesterElectiveRequirement  extends BaseEntity {
    private Integer semester; // Ex: 4
    private Integer minEletivasRequired; // Ex: 4 eletivas mínimas

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_requirements_id")
    private CourseRequirements courseRequirements;
}
