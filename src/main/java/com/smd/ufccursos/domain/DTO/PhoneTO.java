package com.smd.ufccursos.domain.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneTO {
    private String number;
    private String ddd;
}
