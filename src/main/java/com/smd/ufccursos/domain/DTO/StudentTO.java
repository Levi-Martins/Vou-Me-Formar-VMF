package com.smd.ufccursos.domain.DTO;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentTO {
    private String name;
    private String email;
    private Integer registration;
    private UUID courseId;
    private List<PhoneTO> phones;
}
