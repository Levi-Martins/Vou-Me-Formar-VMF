package com.smd.ufccursos.domain.service;

import com.opencsv.CSVReader;
import com.smd.ufccursos.application.exceptions.ValidationErrorDetail;
import com.smd.ufccursos.domain.DTO.request.DisciplineDTORequest;
import com.smd.ufccursos.domain.DTO.response.DisciplineResponseDTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import com.smd.ufccursos.domain.exceptions.CSVImportValidationException;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;
import com.smd.ufccursos.domain.entity.SemesterElectiveRequirement;
import jakarta.validation.ConstraintViolation;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Validator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class CSVImportService {

    private final DisciplineServicePort disciplineServicePort;
    private final Validator validator;
    private final CourseServicePort courseServicePort;


    public CSVImportService(DisciplineServicePort disciplineServicePort, Validator validator, CourseServicePort courseServicePort) {
        this.disciplineServicePort = disciplineServicePort;
        this.validator = validator;
        this.courseServicePort = courseServicePort;
    }

    public void importFromAnyFile(MultipartFile file, UUID courseId) throws Exception {
        String filename = file.getOriginalFilename();

        if (filename != null && (filename.endsWith(".xlsx") || filename.endsWith(".xls"))) {
            importFromExcel(file.getInputStream(), courseId);
        } else {
            importFromCSV(file.getInputStream(), courseId);
        }
    }

    private void importFromExcel(InputStream fileInputStream, UUID courseId) throws Exception {
        Course course = prepareCourseValidation(courseId);
        Set<Integer> allowedElectiveSemesters = getAllowedSemesters(course);
        boolean courseAllowsElectives = !allowedElectiveSemesters.isEmpty();

        List<DisciplineDTORequest> disciplineDTORequests = new ArrayList<>();
        Map<String, String> prerequisiteMapping = new HashMap<>();

        try (Workbook workbook = WorkbookFactory.create(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Pega a primeira aba

            int lineNumber = 0;
            for (Row row : sheet) {
                lineNumber++;
                if (lineNumber == 1) continue;

                if (row.getCell(0) == null || row.getCell(0).toString().trim().isEmpty()) continue;


                String semesterStr = getCellValueAsString(row.getCell(0));
                String code = getCellValueAsString(row.getCell(1));
                String name = getCellValueAsString(row.getCell(2));
                String workloadStr = getCellValueAsString(row.getCell(3));
                String creditsStr = getCellValueAsString(row.getCell(4));
                String nature = getCellValueAsString(row.getCell(5));
                String prerequisites = getCellValueAsString(row.getCell(6));

                processRowData(lineNumber, semesterStr, code, name, workloadStr, creditsStr, nature, prerequisites,
                        course, courseAllowsElectives, allowedElectiveSemesters,
                        disciplineDTORequests, prerequisiteMapping, courseId);
            }
        }catch (Exception e) {
            throw new CSVImportValidationException(
                    "Erro ao ler arquivo Excel",
                    List.of(new ValidationErrorDetail("arquivo", "O arquivo enviado não é um Excel válido ou está corrompido.", e.getMessage()))
            );
        }

        saveDisciplines(disciplineDTORequests, prerequisiteMapping);
    }

    private void importFromCSV(InputStream fileInputStream, UUID courseId) throws Exception {
        Course course = prepareCourseValidation(courseId);
        Set<Integer> allowedElectiveSemesters = getAllowedSemesters(course);
        boolean courseAllowsElectives = !allowedElectiveSemesters.isEmpty();

        List<DisciplineDTORequest> disciplineDTORequests = new ArrayList<>();
        Map<String, String> prerequisiteMapping = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(fileInputStream))) {
            String[] line;
            int lineNumber = 1;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) { isHeader = false; lineNumber++; continue; }

                // Extrair dados do Array
                String semesterStr = line[0];
                String code = line[1];
                String name = line[2];
                String workloadStr = line[3];
                String creditsStr = line[4];
                String nature = line[5];
                String prerequisites = line.length > 6 ? line[6] : "";

                processRowData(lineNumber, semesterStr, code, name, workloadStr, creditsStr, nature, prerequisites,
                        course, courseAllowsElectives, allowedElectiveSemesters,
                        disciplineDTORequests, prerequisiteMapping, courseId);
                lineNumber++;
            }
        }
        saveDisciplines(disciplineDTORequests, prerequisiteMapping);
    }

    private void processRowData(int lineNumber, String semesterStr, String code, String name,
                                String workloadStr, String creditsStr, String nature, String prerequisites,
                                Course course, boolean courseAllowsElectives, Set<Integer> allowedElectiveSemesters,
                                List<DisciplineDTORequest> requests, Map<String, String> mapping, UUID courseId) {

        Integer semester = parseInteger(semesterStr, "semestre");
        Integer workload = parseInteger(workloadStr, "carga horária");
        Integer credits = parseInteger(creditsStr, "créditos");

        code = code != null ? code.trim() : "";
        name = name != null ? name.trim() : "";
        nature = nature != null ? nature.trim() : "";
        prerequisites = prerequisites != null ? prerequisites.trim() : "";

        TypeOfDiscipline type = mapNatureToType(nature);

        // Validações de Regra de Negócio (Eletivas)
        if (type == TypeOfDiscipline.ELETIVA) {
            if (!courseAllowsElectives) {
                throw new CSVImportValidationException("Erro na linha " + lineNumber, List.of(new ValidationErrorDetail("natureza", "O curso não aceita eletivas.", nature)));
            }
            if (!allowedElectiveSemesters.contains(semester)) {
                throw new CSVImportValidationException("Erro na linha " + lineNumber, List.of(new ValidationErrorDetail("semestre", "Semestre " + semester + " não permite eletivas.", semester.toString())));
            }
        }

        DisciplineDTORequest dto = new DisciplineDTORequest();
        dto.setName(name);
        dto.setSemester(semester);
        dto.setDisciplineCode(code);
        dto.setWorkload(workload);
        dto.setClassCredits(credits);
        dto.setTypeOfDiscipline(type);
        dto.setCourseId(courseId);
        dto.setPrerequisiteIds(Collections.emptySet());

        validateDisciplineDTO(dto, lineNumber);

        requests.add(dto);
        mapping.put(code, prerequisites);
    }

    private void saveDisciplines(List<DisciplineDTORequest> requests, Map<String, String> mapping) {
        Map<String, DisciplineResponseDTO> savedDisciplines = new HashMap<>();

        for (DisciplineDTORequest req : requests) {
            DisciplineResponseDTO saved = disciplineServicePort.save(req);
            savedDisciplines.put(saved.getDisciplineCode(), saved);
        }

        for (DisciplineDTORequest req : requests) {
            String preReqStr = mapping.get(req.getDisciplineCode());
            if (preReqStr != null && !preReqStr.isBlank() && !preReqStr.equalsIgnoreCase("sem pré-requisito")) {
                Set<UUID> ids = resolvePrerequisites(preReqStr, savedDisciplines);
                req.setPrerequisiteIds(ids);
                DisciplineResponseDTO saved = savedDisciplines.get(req.getDisciplineCode());
                disciplineServicePort.update(saved.getId(), req);
            }
        }
    }

    private Course prepareCourseValidation(UUID courseId) {
        return courseServicePort.findEntityById(courseId);
    }

    private Set<Integer> getAllowedSemesters(Course course) {
        if (course.getRequirements() != null && course.getRequirements().getSemesterElectiveRequirementList() != null) {
            return course.getRequirements().getSemesterElectiveRequirementList().stream()
                    .map(SemesterElectiveRequirement::getSemester)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                // Remove decimais se for inteiro (ex: 4.0 -> "4")
                if (DateUtil.isCellDateFormatted(cell)) return cell.toString();
                double val = cell.getNumericCellValue();
                if (val == (long) val) return String.format("%d", (long) val);
                return String.valueOf(val);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
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
