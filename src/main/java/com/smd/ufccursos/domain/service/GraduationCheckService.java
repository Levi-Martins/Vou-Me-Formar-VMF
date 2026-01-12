package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;
import com.smd.ufccursos.domain.DTO.response.SemesterElectiveStatus;
import com.smd.ufccursos.domain.entity.*;
import com.smd.ufccursos.domain.exceptions.ObjectNotFoundException;
import com.smd.ufccursos.domain.mapper.GraduationCheckMapper;
import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.GraduationCheckServicePort;

import java.util.*;
import java.util.stream.Collectors;

public class GraduationCheckService implements GraduationCheckServicePort {

    private final CourseRepositoryPort courseRepositoryPort;
    private final DisciplineRepositoryPort disciplineRepositoryPort;

    public GraduationCheckService(CourseRepositoryPort courseRepositoryPort, DisciplineRepositoryPort disciplineRepositoryPort) {
        this.courseRepositoryPort = courseRepositoryPort;
        this.disciplineRepositoryPort = disciplineRepositoryPort;
    }

    @Override
    public GraduationCheckResponse checkGraduationEligibility(GraduationCheckRequest request) {
        Course course = courseRepositoryPort.findById(request.getCourseId())
                .orElseThrow(() -> new ObjectNotFoundException("Course not found"));

        CourseRequirements req = course.getRequirements();
        List<String> missing = new ArrayList<>();

        Set<UUID> completedIds = request.getCompletedDisciplineIds() != null ? request.getCompletedDisciplineIds() : Set.of();

        List<Discipline> allDisciplines = disciplineRepositoryPort.findByCourseId(course.getId());

        Set<UUID> courseDisciplineIds = allDisciplines.stream()
                .map(Discipline::getId)
                .collect(Collectors.toSet());

        List<UUID> extraIds = completedIds.stream()
                .filter(id -> !courseDisciplineIds.contains(id))
                .toList();

        List<Discipline> extraDisciplines = new ArrayList<>();
        if (!extraIds.isEmpty()) {
            extraDisciplines = disciplineRepositoryPort.findAllById(extraIds);
        }

        List<Discipline> mandatory = allDisciplines.stream()
                .filter(d -> d.getTypeOfDiscipline() == TypeOfDiscipline.OBRIGATORIA)
                .toList();

        List<Discipline> completedMandatory = mandatory.stream()
                .filter(d -> completedIds.contains(d.getId()))
                .toList();

        List<String> missingMandatory = mandatory.stream()
                .filter(d -> !completedIds.contains(d.getId()))
                .map(Discipline::getName)
                .toList();

        if (!missingMandatory.isEmpty()) {
            missing.add("Disciplinas obrigatórias pendentes: " + String.join(", ", missingMandatory));
        }

        int completedMandatoryHours = completedMandatory.stream()
                .mapToInt(Discipline::getWorkload)
                .sum();


        List<Discipline> optionalAndEletivas = allDisciplines.stream()
                .filter(d -> d.getTypeOfDiscipline() == TypeOfDiscipline.OPTATIVA
                        || d.getTypeOfDiscipline() == TypeOfDiscipline.ELETIVA)
                .toList();

        int hoursFromCourseDisciplines = optionalAndEletivas.stream()
                .filter(d -> completedIds.contains(d.getId()))
                .mapToInt(Discipline::getWorkload)
                .sum();

        int hoursFromExtraDisciplines = extraDisciplines.stream()
                .mapToInt(Discipline::getWorkload)
                .sum();


        int hoursFromManualInput = request.getCompletedOptionalHours() != null ? request.getCompletedOptionalHours() : 0;

        int totalCompletedOptionalHours = hoursFromCourseDisciplines + hoursFromExtraDisciplines + hoursFromManualInput;

        System.out.println("DEBUG: Horas Curso: " + hoursFromCourseDisciplines);
        System.out.println("DEBUG: Horas Extras: " + hoursFromExtraDisciplines);
        System.out.println("DEBUG: Horas Manuais: " + hoursFromManualInput);
        System.out.println("DEBUG: Total Optativas: " + totalCompletedOptionalHours);

        if (totalCompletedOptionalHours < req.getRequiredOptionalHours()) {
            missing.add("Carga horária optativa insuficiente. Faltam "
                    + (req.getRequiredOptionalHours() - totalCompletedOptionalHours) + "h.");
        }

        List<SemesterElectiveStatus> semesterStatuses = new ArrayList<>();

        if (req.getSemesterElectiveRequirementList() != null && !req.getSemesterElectiveRequirementList().isEmpty()) {
            List<Discipline> eletivas = allDisciplines.stream()
                    .filter(d -> d.getTypeOfDiscipline() == TypeOfDiscipline.ELETIVA)
                    .toList();

            Map<Integer, Long> completedEletivasBySemester = eletivas.stream()
                    .filter(d -> completedIds.contains(d.getId()))
                    .collect(Collectors.groupingBy(Discipline::getSemester, Collectors.counting()));

            for (SemesterElectiveRequirement ser : req.getSemesterElectiveRequirementList()) {
                long completed = completedEletivasBySemester.getOrDefault(ser.getSemester(), 0L);
                boolean fulfilled = completed >= ser.getMinEletivasRequired();

                semesterStatuses.add(
                        SemesterElectiveStatus.builder()
                                .semester(ser.getSemester())
                                .requiredEletivas(ser.getMinEletivasRequired())
                                .completedEletivas(completed)
                                .fulfilled(fulfilled)
                                .build()
                );

                if (!fulfilled) {
                    missing.add(String.format(
                            "Semestre %d: mínimo de %d eletivas, mas apenas %d concluídas.",
                            ser.getSemester(), ser.getMinEletivasRequired(), completed
                    ));
                }
            }
        }

        // --- Horas complementares ---
        int compHours = request.getCompletedComplementaryHours() != null ? request.getCompletedComplementaryHours() : 0;

        if (compHours < req.getRequiredComplementaryHours()) {
            missing.add("Horas complementares insuficientes. Faltam "
                    + (req.getRequiredComplementaryHours() - compHours) + "h.");
        }

        // --- TCC / Estágio / Extensão ---
        if (req.getTccHours() > 0 && !Boolean.TRUE.equals(request.getTccCompleted())) {
            missing.add("TCC não concluído.");
        }

        int internshipHours = request.getInternshipCompleted() != null ? request.getInternshipCompleted() : 0;
        if (req.getInternshipHours() > 0 && internshipHours == 0) {
            missing.add("Estágio não concluído.");
        }

        int extensionHours = request.getExtensionCompleted() != null ? request.getExtensionCompleted() : 0;
        if (req.getExtensionHours() > 0 && extensionHours == 0) {
            missing.add("Extensão não concluída.");
        }

        boolean eligible = missing.isEmpty();

        GraduationCheckResponse response = GraduationCheckMapper.toResponse(
                eligible,
                missing,
                missingMandatory,
                mandatory.size(),
                completedMandatory.size(),
                completedMandatoryHours,
                req,
                totalCompletedOptionalHours,
                compHours,
                Boolean.TRUE.equals(request.getTccCompleted()),
                internshipHours,
                extensionHours,
                semesterStatuses
        );

        response.setUnknownDisciplines(request.getUnknownDisciplines() != null ? request.getUnknownDisciplines() : new ArrayList<>());

        return response;
    }
}