package com.smd.ufccursos.domain.mapper;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.PythonDiscipleneDTOResponse;
import com.smd.ufccursos.domain.DTO.response.PythonPdfResponseDTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PdfGraduationCheckMapper {

    private final DisciplineRepositoryPort disciplineRepositoryPort;

    public PdfGraduationCheckMapper(DisciplineRepositoryPort disciplineRepositoryPort) {
        this.disciplineRepositoryPort = disciplineRepositoryPort;
    }

    public GraduationCheckRequest toGraduationCheckRequest(PythonPdfResponseDTO pythonData, UUID courseId) {

        System.out.println("--- INICIO DEBUG MAPPER ---");
        System.out.println("Total disciplinas vindas do Python: " + pythonData.getDisciplines().size());

        // 1. Encontrar os IDs das disciplinas concluídas
        Set<String> approvedCodes = pythonData.getDisciplines().stream()
                .filter(d -> "APROVADO MÉDIA".equalsIgnoreCase(d.getSituation()) ||
                        "APROVADO".equalsIgnoreCase(d.getSituation())) // Seja flexível com a situacao
                .map(PythonDiscipleneDTOResponse::getDisciplineCode)
                .collect(Collectors.toSet());

        System.out.println("Códigos considerados APROVADOS (Enviados para busca no Banco): " + approvedCodes);

        // 2. Buscar no BD as disciplinas do curso que batem com os códigos aprovados
        List<Discipline> disciplinesCompleted = disciplineRepositoryPort.findByCourseIdAndDisciplineCodeIn(courseId, approvedCodes);

        Set<UUID> completedDisciplineIds = disciplinesCompleted.stream()
                .map(Discipline::getId)
                .collect(Collectors.toSet());

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
