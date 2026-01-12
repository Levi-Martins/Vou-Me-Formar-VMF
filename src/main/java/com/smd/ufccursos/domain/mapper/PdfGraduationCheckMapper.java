package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.PythonPdfResponseDTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PdfGraduationCheckMapper {

    private final DisciplineRepositoryPort disciplineRepositoryPort;

    public PdfGraduationCheckMapper(DisciplineRepositoryPort disciplineRepositoryPort) {
        this.disciplineRepositoryPort = disciplineRepositoryPort;
    }

    public GraduationCheckRequest toGraduationCheckRequest(PythonPdfResponseDTO pythonData, UUID courseId) {

        var approvedPythonDisciplines = pythonData.getDisciplines().stream()
                .filter(d -> isApproved(d.getSituation()))
                .toList();

        Set<String> allApprovedCodes = approvedPythonDisciplines.stream()
                .map(d -> d.getDisciplineCode().trim())
                .collect(Collectors.toSet());

        List<Discipline> courseDisciplines = disciplineRepositoryPort.findByCourseIdAndDisciplineCodeIn(courseId, allApprovedCodes);

        Set<String> foundCodes = courseDisciplines.stream()
                .map(Discipline::getDisciplineCode)
                .collect(Collectors.toSet());

        List<String> missingCodes = allApprovedCodes.stream()
                .filter(code -> !foundCodes.contains(code))
                .toList();

        List<Discipline> extraDisciplines = new ArrayList<>();

        if (!missingCodes.isEmpty()) {
            extraDisciplines = disciplineRepositoryPort.findByDisiplineCodeIn(new HashSet<>(missingCodes));
        }

        extraDisciplines.forEach(d -> foundCodes.add(d.getDisciplineCode()));

        Set<UUID> completedDisciplineIds = new HashSet<>();
        courseDisciplines.forEach(d -> completedDisciplineIds.add(d.getId()));
        extraDisciplines.forEach(d -> completedDisciplineIds.add(d.getId()));

        List<String> unknownDisciplinesList = new ArrayList<>();
        for (var pyDisc : approvedPythonDisciplines) {
            String code = pyDisc.getDisciplineCode().trim();
            if (!foundCodes.contains(code)) {
                unknownDisciplinesList.add(code + " - " + pyDisc.getDisciplineName());
            }
        }

        var sumary = pythonData.getWorkloadSummary();

        Integer complementaryHours = parseIntSafe(
                sumary.getComplementaryActivities().getCompleted()
        );

        Integer internshipHours = parseIntSafe(
                sumary.getInternship().getCompleted()
        );

        Integer hoursExtension = parseIntSafe(
                sumary.getExtension().getCompleted()
        );

        Boolean completeTcc = parseIntSafe(
                sumary.getFinalProject().getCompleted()
        ) > 0;

        return GraduationCheckRequest.builder()
                .courseId(courseId)
                .completedDisciplineIds(completedDisciplineIds)
                .completedComplementaryHours(complementaryHours)
                .tccCompleted(completeTcc)
                .internshipCompleted(internshipHours)
                .extensionCompleted(hoursExtension)
                .completedOptionalHours(0)
                .unknownDisciplines(unknownDisciplinesList)
                .build();
    }

    private boolean isApproved(String situation) {
        if (situation == null) return false;
        String s = situation.trim().toUpperCase();
        return s.contains("APROVADO") || s.contains("DISPENSADO") || s.contains("APROVEITAMENTO");
    }

    private Integer parseIntSafe(String valor) {
        if (valor == null || valor.isBlank() || valor.equals("--")) {
            return 0;
        }
        try {
            double d = Double.parseDouble(valor);
            return (int) d;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}