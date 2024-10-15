package com.smd.ufccursos.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Student extends BaseEntity {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Column(unique = true)
    private Integer registration;

    @JsonIgnore
    private LocalDate registrationDate;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDate.now();
    }

}
