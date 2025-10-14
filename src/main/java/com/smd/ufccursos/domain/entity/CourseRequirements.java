package com.smd.ufccursos.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseRequirements extends BaseEntity {

    // Horas obrigatórias e optativas
    private Integer requiredMandatoryHours;
    private Integer requiredOptionalHours;
    private Integer requiredComplementaryHours;

    // TCC
    private Boolean requiresTcc;
    private Integer tccHours;

    // Estágio
    private Boolean requiresInternship;
    private Integer internshipHours;

    // Extensão
    private Boolean requiresExtension;
    private Integer extensionHours;

    // Eletivas
    private Boolean hasEletives;

    public Integer getTotalRequiredHours() {
        return requiredMandatoryHours
                + requiredOptionalHours
                + requiredComplementaryHours
                + (requiresTcc != null && requiresTcc ? tccHours : 0)
                + (requiresInternship != null && requiresInternship ? internshipHours : 0)
                + (requiresExtension != null && requiresExtension ? extensionHours : 0);
    }
}
