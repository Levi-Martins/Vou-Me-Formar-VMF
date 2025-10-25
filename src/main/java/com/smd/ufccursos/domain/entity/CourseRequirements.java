package com.smd.ufccursos.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseRequirements extends BaseEntity {

    private Integer requiredMandatoryHours;       // disciplinas obrigatórias
    private Integer requiredOptionalHours;        // disciplinas optativas
    private Integer requiredComplementaryHours;   // atividades complementares
    private Integer tccHours;                     // 0 = não exige TCC
    private Integer internshipHours;              // 0 = não exige estágio
    private Integer extensionHours;               // 0 = não exige extensão


    @OneToMany(mappedBy = "courseRequirements", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SemesterElectiveRequirement> semesterElectiveRequirementList;

    // Soma total de horas exigidas para formar
    public Integer getTotalRequiredHours() {
        return safe(requiredMandatoryHours)
                + safe(requiredOptionalHours)
                + safe(requiredComplementaryHours)
                + safe(tccHours)
                + safe(internshipHours)
                + safe(extensionHours);
    }

    private int safe(Integer value) {
        return value != null ? value : 0;
    }
}
