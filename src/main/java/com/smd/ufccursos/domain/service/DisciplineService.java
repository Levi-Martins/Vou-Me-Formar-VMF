package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.DisciplineTO;
import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DisciplineService implements DisciplineServicePort {

    private final DisciplineRepositoryPort disciplineRepositoryPort;
    private final CourseServicePort courseServicePort;

    public DisciplineService(DisciplineRepositoryPort disciplineRepositoryPort, CourseServicePort courseServicePort) {
        this.disciplineRepositoryPort = disciplineRepositoryPort;
        this.courseServicePort = courseServicePort;
    }

    @Override
    public PageTO<Discipline> findAll(PaginationTO paginationTO) {
        return disciplineRepositoryPort.findAll(paginationTO);
    }

    @Override
    public Discipline findById(UUID id) {
        Optional<Discipline> discipline = disciplineRepositoryPort.findById(id);
        if (discipline.isEmpty()) {
            throw new RuntimeException("Discipline not found");
        }
        return discipline.get();    }

    @Override
    public Discipline save(DisciplineTO disciplineTO) {
        Course course = courseServicePort.findById(disciplineTO.getCourseId());

        Set<Discipline> prerequisites = (disciplineTO.getPrerequisiteIds() != null) ?
                disciplineTO.getPrerequisiteIds().stream()
                        .map(this::findById)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        Discipline discipline = Discipline.builder()
                .name(disciplineTO.getName())
                .typeOfDiscipline(disciplineTO.getTypeOfDiscipline())
                .workload(disciplineTO.getWorkload())
                .classCredits(disciplineTO.getClassCredits())
                .description(disciplineTO.getDescription())
                .semester(disciplineTO.getSemester())
                .course(course)
                .prerequisites(prerequisites)
                .build();

        return disciplineRepositoryPort.save(discipline);
    }


    @Override
    public Discipline update(UUID id, DisciplineTO disciplineTO) {
        Discipline discipline = findById(id);

        discipline.setName(disciplineTO.getName());
        discipline.setTypeOfDiscipline(disciplineTO.getTypeOfDiscipline());
        discipline.setWorkload(disciplineTO.getWorkload());
        discipline.setClassCredits(disciplineTO.getClassCredits());
        discipline.setDescription(disciplineTO.getDescription());
        discipline.setSemester(disciplineTO.getSemester());

        Course course = courseServicePort.findById(disciplineTO.getCourseId());
        discipline.setCourse(course);

        Set<Discipline> prerequisites = (disciplineTO.getPrerequisiteIds() != null) ?
                disciplineTO.getPrerequisiteIds().stream()
                        .map(this::findById)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        discipline.setPrerequisites(prerequisites);

        return disciplineRepositoryPort.save(discipline);
    }


    @Override
    public void deleteById(UUID id) {
        disciplineRepositoryPort.deleteById(id);
    }
}
