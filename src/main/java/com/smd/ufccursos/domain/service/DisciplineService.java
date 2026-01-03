package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.request.DisciplineDTORequest;
import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.response.DisciplineResponseDTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.exceptions.BusinessRuleException;
import com.smd.ufccursos.domain.exceptions.ObjectNotFoundException;
import com.smd.ufccursos.domain.mapper.DisciplineMapper;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;

import java.util.*;
import java.util.stream.Collectors;

public class DisciplineService implements DisciplineServicePort {

    private final DisciplineRepositoryPort disciplineRepositoryPort;
    private final CourseServicePort courseServicePort;

    public DisciplineService(DisciplineRepositoryPort disciplineRepositoryPort, CourseServicePort courseServicePort) {
        this.disciplineRepositoryPort = disciplineRepositoryPort;
        this.courseServicePort = courseServicePort;
    }

    public PageTO<DisciplineResponseDTO> findAll(PaginationTO paginationTO) {
        PageTO<Discipline> disciplines = disciplineRepositoryPort.findAll(paginationTO);
        List<DisciplineResponseDTO> response = disciplines.getContent().stream()
                .map(DisciplineResponseDTO::new)
                .collect(Collectors.toList());
        return PageTO.of(disciplines, response);
    }

    @Override
    public DisciplineResponseDTO findById(UUID id) {
        Optional<Discipline> discipline = disciplineRepositoryPort.findById(id);
        if (discipline.isEmpty()) {
            throw new ObjectNotFoundException("Discipline not found");
        }
        return DisciplineMapper.toResponse(discipline.get());
    }

    public Discipline findByIdEntity(UUID id) {
        Optional<Discipline> discipline = disciplineRepositoryPort.findById(id);
        if (discipline.isEmpty()) {
            throw new ObjectNotFoundException("Discipline not found");
        }
        return discipline.get();
    }

    @Override
    public DisciplineResponseDTO save(DisciplineDTORequest disciplineDTORequest) {
        Discipline discipline = DisciplineMapper.toEntity(disciplineDTORequest);

        Course course = courseServicePort.findEntityById(disciplineDTORequest.getCourseId());
        discipline.setCourse(course);

        Set<Discipline> prerequisites = (disciplineDTORequest.getPrerequisiteIds() != null)
                ? disciplineDTORequest.getPrerequisiteIds().stream()
                .map(this::findByIdEntity)
                .collect(Collectors.toSet())
                : Collections.emptySet();
        discipline.setPrerequisites(prerequisites);

        Discipline saved = disciplineRepositoryPort.save(discipline);
        return DisciplineMapper.toResponse(saved);
    }


    @Override
    public DisciplineResponseDTO update(UUID id, DisciplineDTORequest disciplineDTORequest) {
        Discipline discipline = findByIdEntity(id);

        discipline.setName(disciplineDTORequest.getName());
        discipline.setTypeOfDiscipline(disciplineDTORequest.getTypeOfDiscipline());
        discipline.setWorkload(disciplineDTORequest.getWorkload());
        discipline.setClassCredits(disciplineDTORequest.getClassCredits());
        discipline.setDescription(disciplineDTORequest.getDescription());
        discipline.setSemester(disciplineDTORequest.getSemester());
        discipline.setDisciplineCode(disciplineDTORequest.getDisciplineCode());

        Course course = courseServicePort.findEntityById(disciplineDTORequest.getCourseId());
        discipline.setCourse(course);

        Set<Discipline> prerequisites = (disciplineDTORequest.getPrerequisiteIds() != null) ?
                disciplineDTORequest.getPrerequisiteIds().stream()
                        .map(this::findByIdEntity)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        discipline.setPrerequisites(prerequisites);
        Discipline updatedDiscipline = disciplineRepositoryPort.save(discipline);
        return DisciplineMapper.toResponse(updatedDiscipline);
    }


    @Override
    public void deleteById(UUID id) {
        Discipline disciplineToDelete = findByIdEntity(id);

        List<Discipline> dependents = disciplineRepositoryPort.findByPrerequisiteId(id);

        if (!dependents.isEmpty()) {
            String dependentCodes = dependents.stream()
                    .map(Discipline::getDisciplineCode)
                    .collect(Collectors.joining(", "));

            throw new BusinessRuleException(
                    "Não é possível excluir a disciplina " + disciplineToDelete.getDisciplineCode() +
                            " pois ela é pré-requisito das seguintes disciplinas: " + dependentCodes
            );
        }

        disciplineRepositoryPort.deleteById(id);
    }

    @Override
    public Discipline findByDisciplineCode(String code) {
        Optional<Discipline> discipline = disciplineRepositoryPort.findByDisciplineCode(code);
        if (discipline.isEmpty()) {
            throw new RuntimeException("Discipline not found");
        }
        return discipline.get();
    }
}
