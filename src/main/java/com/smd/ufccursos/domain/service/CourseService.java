package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.request.CourseDTORequest;
import com.smd.ufccursos.domain.DTO.request.CourseRequirementsDTORequest;
import com.smd.ufccursos.domain.DTO.response.CourseDTOResponse;
import com.smd.ufccursos.domain.DTO.response.CourseRequirementsDTOResponse;
import com.smd.ufccursos.domain.DTO.response.ElectiveRequirementDTOResponse;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.entity.CourseRequirements;
import com.smd.ufccursos.domain.entity.SemesterElectiveRequirement;
import com.smd.ufccursos.domain.exceptions.BusinessRuleException;
import com.smd.ufccursos.domain.exceptions.ObjectNotFoundException;
import com.smd.ufccursos.domain.mapper.CourseMapper;
import com.smd.ufccursos.domain.mapper.CourseRequirementsMapper;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CourseService implements CourseServicePort {

    private final CourseRepositoryPort courseRepositoryPort;

    public CourseService(CourseRepositoryPort courseRepositoryPort) {
        this.courseRepositoryPort = courseRepositoryPort;
    }


    @Override
    public PageTO<CourseDTOResponse> findAll(PaginationTO paginationTO) {
        PageTO<Course> coursePage = courseRepositoryPort.findAll(paginationTO);

        List<CourseDTOResponse> courseDTOs = coursePage.getContent().stream()
                .map(CourseMapper::toResponse)
                .toList();

        return PageTO.of(coursePage, courseDTOs);
    }

    @Override
    public CourseDTOResponse findById(UUID id) {
        Optional<Course> course = courseRepositoryPort.findById(id);
        if (course.isEmpty()) {
            throw new ObjectNotFoundException("Course not found ");
        }
        return CourseMapper.toResponse(course.get());
    }

    @Override
    public Course findEntityById(UUID id) {
        return courseRepositoryPort.findById(id).orElseThrow(() -> new ObjectNotFoundException("Course not found "));
    }

    @Override
    public CourseDTOResponse save(CourseDTORequest courseDTORequest) {
        Course course = CourseMapper.toEntity(courseDTORequest);
        validateDuplicateSemesters(course.getRequirements());
        Course savedCourse = courseRepositoryPort.save(course);
        return CourseMapper.toResponse(savedCourse);
    }

    @Override
    public CourseDTOResponse update(UUID id, CourseDTORequest courseDTORequest) {
        Course existingCourse = findEntityById(id);

        existingCourse.setName(courseDTORequest.getName());
        existingCourse.setDepartment(courseDTORequest.getDepartment());

        if (courseDTORequest.getRequirements() != null) {
            CourseRequirements newRequirements =
                    CourseRequirementsMapper.toEntity(courseDTORequest.getRequirements());

            validateDuplicateSemesters(newRequirements);

            if (newRequirements.getSemesterElectiveRequirementList() != null) {
                newRequirements.getSemesterElectiveRequirementList()
                        .forEach(e -> e.setCourseRequirements(newRequirements));
            }

            existingCourse.setRequirements(newRequirements);
        }

        Course updatedCourse = courseRepositoryPort.save(existingCourse);
        return CourseMapper.toResponse(updatedCourse);
    }

    @Override
    public void deleteById(UUID id) {
        courseRepositoryPort.deleteById(id);
    }

    private void validateDuplicateSemesters(CourseRequirements requirements) {
        if (requirements == null || requirements.getSemesterElectiveRequirementList() == null) return;

        List<SemesterElectiveRequirement> electives = requirements.getSemesterElectiveRequirementList();

        var duplicates = electives.stream()
                .collect(Collectors.groupingBy(SemesterElectiveRequirement::getSemester, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        if (!duplicates.isEmpty()) {
            throw new BusinessRuleException(
                    "Semestres duplicados encontrados nos requisitos eletivos: " + duplicates
            );
        }
    }

}
