package com.smd.ufccursos.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Phone extends BaseEntity{

    @Column(nullable = false)
    private String ddd;

    @Column(nullable = false)
    private String number;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
