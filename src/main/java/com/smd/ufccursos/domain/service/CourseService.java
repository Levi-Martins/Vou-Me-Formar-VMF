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
import com.smd.ufccursos.domain.exceptions.ObjectNotFoundException;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CourseService implements CourseServicePort {

    private final CourseRepositoryPort courseRepositoryPort;

    public CourseService(CourseRepositoryPort courseRepositoryPort) {
        this.courseRepositoryPort = courseRepositoryPort;
    }


    @Override
    public PageTO<Course> findAll(PaginationTO paginationTO) {
        return courseRepositoryPort.findAll(paginationTO);
    }

    @Override
    public Course findById(UUID id) {
        Optional<Course> course = courseRepositoryPort.findById(id);
        if (course.isEmpty()) {
            throw new ObjectNotFoundException("Course not found ");
        }
        return course.get();
    }

    @Override
    public CourseDTOResponse save(CourseDTORequest courseDTORequest) {
        CourseRequirements requirements = mapToCourseRequirements(courseDTORequest.getRequirements());

        Course course = Course.builder()
                .name(courseDTORequest.getName())
                .department(courseDTORequest.getDepartment())
                .requirements(requirements)
                .build();

        if (requirements.getSemesterElectiveRequirementList() != null) {
            requirements.getSemesterElectiveRequirementList()
                    .forEach(e -> e.setCourseRequirements(requirements));
        }

        Course savedCourse = courseRepositoryPort.save(course);
        return mapToDTO(savedCourse);
    }

    @Override
    public Course update(UUID id, CourseDTORequest courseDTORequest) {
        Course courseToUpdate = findById(id);
        courseToUpdate.setName(courseDTORequest.getName());
        courseToUpdate.setDepartment(courseDTORequest.getDepartment());

        if (courseDTORequest.getRequirements() != null) {
            CourseRequirements newRequirements = mapToCourseRequirements(courseDTORequest.getRequirements());
            courseToUpdate.setRequirements(newRequirements);

            if (newRequirements.getSemesterElectiveRequirementList() != null) {
                newRequirements.getSemesterElectiveRequirementList()
                        .forEach(e -> e.setCourseRequirements(newRequirements));
            }
        }

        return courseRepositoryPort.save(courseToUpdate);
    }

    @Override
    public void deleteById(UUID id) {
        courseRepositoryPort.deleteById(id);
    }

    // 🔧 Método auxiliar para montar CourseRequirements
    private CourseRequirements mapToCourseRequirements(CourseRequirementsDTORequest dto) {
        if (dto == null) return null;

        CourseRequirements requirements = CourseRequirements.builder()
                .requiredMandatoryHours(dto.getRequiredMandatoryHours())
                .requiredOptionalHours(dto.getRequiredOptionalHours())
                .requiredComplementaryHours(dto.getRequiredComplementaryHours())
                .tccHours(dto.getTccHours())
                .internshipHours(dto.getInternshipHours())
                .extensionHours(dto.getExtensionHours())
                .build();

        // 🔁 Mapeia eletivas por semestre, se houver
        if (dto.getElectiveRequirements() != null && !dto.getElectiveRequirements().isEmpty()) {
            List<SemesterElectiveRequirement> electiveList = dto.getElectiveRequirements().stream()
                    .map(er -> {
                        SemesterElectiveRequirement entity = new SemesterElectiveRequirement();
                        entity.setSemester(er.getSemester());
                        entity.setMinEletivasRequired(er.getMinRequired());
                        return entity;
                    })
                    .collect(Collectors.toList());

            requirements.setSemesterElectiveRequirementList(electiveList);
        }

        return requirements;
    }

    // 🔧 Mapeamento para DTO de resposta
    private CourseDTOResponse mapToDTO(Course course) {
        CourseDTOResponse dto = new CourseDTOResponse();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDepartment(course.getDepartment());

        if (course.getRequirements() != null) {
            CourseRequirementsDTOResponse reqDto = new CourseRequirementsDTOResponse();
            reqDto.setRequiredMandatoryHours(course.getRequirements().getRequiredMandatoryHours());
            reqDto.setRequiredOptionalHours(course.getRequirements().getRequiredOptionalHours());
            reqDto.setRequiredComplementaryHours(course.getRequirements().getRequiredComplementaryHours());
            reqDto.setTccHours(course.getRequirements().getTccHours());
            reqDto.setInternshipHours(course.getRequirements().getInternshipHours());
            reqDto.setExtensionHours(course.getRequirements().getExtensionHours());

            if (course.getRequirements().getSemesterElectiveRequirementList() != null) {
                List<ElectiveRequirementDTOResponse> electives = course.getRequirements()
                        .getSemesterElectiveRequirementList().stream()
                        .map(e -> {
                            ElectiveRequirementDTOResponse erDto = new ElectiveRequirementDTOResponse();
                            erDto.setSemester(e.getSemester());
                            erDto.setMinEletivasRequired(e.getMinEletivasRequired());
                            return erDto;
                        }).collect(Collectors.toList());
                reqDto.setSemesterElectiveRequirementList(electives);
            }

            dto.setRequirements(reqDto);
        }

        return dto;
    }
}
