package com.smd.ufccursos.domain.entity;

import jakarta.persistence.*;

@Entity
public class Phone extends BaseEntity{

    @Column(nullable = false)
    private String ddd;

    @Column(nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
