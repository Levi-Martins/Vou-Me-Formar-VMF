package com.smd.ufccursos.domain.DTO.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemesterElectiveStatus {
    private int semester;
    private int requiredEletivas;
    private long completedEletivas;
    private boolean fulfilled;
}
