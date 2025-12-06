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

        // --- Disciplinas obrigatórias ---
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

        // --- Optativas + Eletivas ---
        List<Discipline> optionalAndEletivas = allDisciplines.stream()
                .filter(d -> d.getTypeOfDiscipline() == TypeOfDiscipline.OPTATIVA
                        || d.getTypeOfDiscipline() == TypeOfDiscipline.ELETIVA)
                .toList();

        System.out.println("--- INICIO DEBUG SERVICE ---");
        System.out.println("Total de Optativas/Eletivas cadastradas no curso: " + optionalAndEletivas.size());

        // Logar quais estão sendo somadas
        int debugSoma = 0;
        for (Discipline d : optionalAndEletivas) {
            if (completedIds.contains(d.getId())) {
                System.out.println("Somando Optativa: " + d.getDisciplineCode() + " (" + d.getName() + ") - " + d.getWorkload() + "h");
                debugSoma += d.getWorkload();
            }
        }
        System.out.println("Soma Total Calculada no Loop Debug: " + debugSoma);
        int completedOptionalHours = optionalAndEletivas.stream()
                .filter(d -> completedIds.contains(d.getId()))
                .mapToInt(Discipline::getWorkload)
                .sum();

        if (completedOptionalHours < req.getRequiredOptionalHours()) {
            missing.add("Carga horária optativa insuficiente. Faltam "
                    + (req.getRequiredOptionalHours() - completedOptionalHours) + "h.");
        }

        List<SemesterElectiveStatus> semesterStatuses = new ArrayList<>();

        // --- Eletivas por semestre ---
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
        if (request.getCompletedComplementaryHours() < req.getRequiredComplementaryHours()) {
            missing.add("Horas complementares insuficientes. Faltam "
                    + (req.getRequiredComplementaryHours() - request.getCompletedComplementaryHours()) + "h.");
        }

        // --- TCC / Estágio / Extensão ---
        if (req.getTccHours() > 0 && (request.getTccCompleted() == null || !request.getTccCompleted())) {
            missing.add("TCC não concluído.");
        }
        if (req.getInternshipHours() > 0 && (request.getInternshipCompleted() == null || request.getInternshipCompleted() == 0)) {
            missing.add("Estágio não concluído.");
        }
        if (req.getExtensionHours() > 0 && (request.getExtensionCompleted() == null || request.getExtensionCompleted() == 0)) {
            missing.add("Extensão não concluída.");
        }

        boolean eligible = missing.isEmpty();

        return GraduationCheckMapper.toResponse(
                eligible,
                missing,
                missingMandatory,
                mandatory.size(),
                completedMandatory.size(),
                completedMandatoryHours,
                req,
                completedOptionalHours,
                request.getCompletedComplementaryHours(),
                Boolean.TRUE.equals(request.getTccCompleted()),
                request.getInternshipCompleted(),
                request.getExtensionCompleted(),
                semesterStatuses
        );
    }

}
