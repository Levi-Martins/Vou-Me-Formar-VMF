package com.smd.ufccursos.domain.service;

import com.opencsv.CSVReader;
import com.smd.ufccursos.application.exceptions.ValidationErrorDetail;
import com.smd.ufccursos.domain.DTO.request.DisciplineDTORequest;
import com.smd.ufccursos.domain.DTO.response.DisciplineResponseDTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import com.smd.ufccursos.domain.exceptions.CSVImportValidationException;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CSVImportService {

    private final DisciplineServicePort disciplineServicePort;
    private final Validator validator;


    public CSVImportService(DisciplineServicePort disciplineServicePort, Validator validator) {
        this.disciplineServicePort = disciplineServicePort;
        this.validator = validator;
    }

    public void importFromCSV(InputStream fileInputStream, UUID courseId) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(fileInputStream))) {
            String[] line;
            Map<String, DisciplineResponseDTO> savedDisciplines = new HashMap<>();
            List<DisciplineDTORequest> disciplineDTORequests = new ArrayList<>();
            Map<String, String> prerequisiteMapping = new HashMap<>();

            boolean isHeader = true;
            int lineNumber = 1;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    lineNumber++;
                    continue;
                }

                Integer semester = parseInteger(line[0], "semestre");
                String code = line[1].trim();
                String name = line[2].trim();
                Integer workload = parseInteger(line[3], "carga horária");
                Integer credits = parseInteger(line[4], "créditos");
                String nature = line[5].trim();
                String prerequisites = line.length > 6 ? line[6].trim() : "";

                DisciplineDTORequest dto = new DisciplineDTORequest();
                dto.setName(name);
                dto.setSemester(semester);
                dto.setDisciplineCode(code);
                dto.setWorkload(workload);
                dto.setClassCredits(credits);
                dto.setTypeOfDiscipline(mapNatureToType(nature));
                dto.setCourseId(courseId);
                dto.setPrerequisiteIds(Collections.emptySet());

                validateDisciplineDTO(dto, lineNumber);

                disciplineDTORequests.add(dto);
                prerequisiteMapping.put(code, prerequisites);

                lineNumber++;
            }

            for (DisciplineDTORequest disciplineDTORequest : disciplineDTORequests) {
                validateDisciplineDTO(disciplineDTORequest, lineNumber);
                DisciplineResponseDTO savedDiscipline = disciplineServicePort.save(disciplineDTORequest);
                savedDisciplines.put(savedDiscipline.getDisciplineCode(), savedDiscipline);
            }

            for (DisciplineDTORequest disciplineDTORequest : disciplineDTORequests) {
                String prerequisites = prerequisiteMapping.get(disciplineDTORequest.getDisciplineCode());

                if (prerequisites != null && !prerequisites.isBlank() && !prerequisites.equalsIgnoreCase("sem pré-requisito")) {
                    Set<UUID> prerequisiteIds = resolvePrerequisites(prerequisites, savedDisciplines);

                    disciplineDTORequest.setPrerequisiteIds(prerequisiteIds);

                    DisciplineResponseDTO savedDiscipline = savedDisciplines.get(disciplineDTORequest.getDisciplineCode());
                    disciplineServicePort.update(savedDiscipline.getId(), disciplineDTORequest);
                }
            }
        }
    }

    private Integer parseInteger(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new CSVImportValidationException(
                    "Erro de validação no campo '" + fieldName + "'",
                    List.of(new ValidationErrorDetail(fieldName, "Campo vazio no CSV", value))
            );
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new CSVImportValidationException(
                    "Erro de validação no campo '" + fieldName + "'",
                    List.of(new ValidationErrorDetail(fieldName, "Valor inválido: " + value, value))
            );
        }
    }

    private TypeOfDiscipline mapNatureToType(String nature) {
        if (nature == null || nature.isBlank()) {
            throw new CSVImportValidationException(
                    "Natureza da disciplina não pode ser nula ou vazia.",
                    List.of(new ValidationErrorDetail("typeOfDiscipline", "Campo vazio ou nulo", nature))
            );
        }

        switch (nature.trim().toUpperCase()) {
            case "OBRIGATÓRIA":
                return TypeOfDiscipline.OBRIGATORIA;
            case "ELETIVA":
                return TypeOfDiscipline.ELETIVA;
            case "OPTATIVA":
                return TypeOfDiscipline.OPTATIVA;
            default:
                throw new CSVImportValidationException(
                        "Natureza desconhecida: " + nature,
                        List.of(new ValidationErrorDetail("typeOfDiscipline", "Valor não reconhecido", nature))
                );
        }
    }

    private Set<UUID> resolvePrerequisites(String prerequisites, Map<String, DisciplineResponseDTO> disciplineMap) {
        if (prerequisites == null || prerequisites.isBlank() || prerequisites.equalsIgnoreCase("sem pré-requisito")) {
            return Collections.emptySet();
        }

        Set<UUID> prerequisiteIds = new HashSet<>();
        String[] parts = prerequisites.split("\\|");

        for (String part : parts) {
            String code = part.split("–")[0].trim();
            DisciplineResponseDTO prerequisite = disciplineMap.get(code);
            if (prerequisite != null) {
                prerequisiteIds.add(prerequisite.getId());
            } else {
                throw new IllegalArgumentException("Pré-requisito não encontrado: " + code);
            }
        }
        return prerequisiteIds;
    }

    private void validateDisciplineDTO(DisciplineDTORequest dto, int lineNumber) {
        Set<ConstraintViolation<DisciplineDTORequest>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            List<ValidationErrorDetail> erros = violations.stream()
                    .map(violation -> new ValidationErrorDetail(
                            violation.getPropertyPath().toString() + " (linha " + lineNumber + ")",
                            violation.getMessage(),
                            violation.getInvalidValue()
                    ))
                    .toList();

            throw new CSVImportValidationException(
                    "Erros de validação encontrados na planilha CSV.",
                    erros
            );
        }
    }
}
