package com.smd.ufccursos.domain.DTO.request;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraduationCheckRequest {
    private UUID courseId;
    private Set<UUID> completedDisciplineIds;
    private Integer completedComplementaryHours;
    private Boolean tccCompleted;
    private Boolean internshipCompleted;
    private Boolean extensionCompleted;
}
