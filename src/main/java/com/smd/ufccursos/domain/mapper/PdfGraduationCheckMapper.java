package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.PythonDiscipleneDTOResponse;
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

        // 1. Extrair todos os códigos APROVADOS do PDF e limpar espaços
        Set<String> allApprovedCodes = pythonData.getDisciplines().stream()
                .filter(d -> isApproved(d.getSituation()))
                .map(d -> d.getDisciplineCode().trim())
                .collect(Collectors.toSet());

        // 2. ESTRATÉGIA MISTA:
        // Passo A: Busca Prioritária (Apenas no curso do aluno)
        List<Discipline> courseDisciplines = disciplineRepositoryPort.findByCourseIdAndDisciplineCodeIn(courseId, allApprovedCodes);

        // Passo B: Identificar o que faltou (quais códigos do PDF não vieram do banco?)
        Set<String> foundCodes = courseDisciplines.stream()
                .map(Discipline::getDisciplineCode)
                .collect(Collectors.toSet());

        List<String> missingCodes = allApprovedCodes.stream()
                .filter(code -> !foundCodes.contains(code))
                .toList();

        List<Discipline> extraDisciplines = new ArrayList<>();

        // Passo C: Se houve faltantes, busca globalmente
        if (!missingCodes.isEmpty()) {
            extraDisciplines = disciplineRepositoryPort.findByDisiplineCodeIn(new HashSet<>(missingCodes));
        }

        // 3. Juntar todos os IDs encontrados
        Set<UUID> completedDisciplineIds = new HashSet<>();
        courseDisciplines.forEach(d -> completedDisciplineIds.add(d.getId()));
        extraDisciplines.forEach(d -> completedDisciplineIds.add(d.getId()));

        // 3. Extrair horas do resumo
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
                .build();
    }

    private boolean isApproved(String situation) {
        if (situation == null) return false;
        String s = situation.trim().toUpperCase();
        return s.contains("APROVADO") || s.contains("DISPENSADO") || s.contains("APROVEITAMENTO");
    }

    // Helper para converter "64.00" ou "0" para Integer
    private Integer parseIntSafe(String valor) {
        if (valor == null || valor.isBlank() || valor.equals("--")) {
            return 0;
        }
        try {
            // Remove o ".00" se existir
            double d = Double.parseDouble(valor);
            return (int) d;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
