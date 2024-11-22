package com.smd.ufccursos.domain.service;

import com.opencsv.CSVReader;
import com.smd.ufccursos.domain.DTO.request.DisciplineTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CSVImportService {

    private final DisciplineServicePort disciplineServicePort;

    public CSVImportService(DisciplineServicePort disciplineServicePort) {
        this.disciplineServicePort = disciplineServicePort;
    }

    public void importFromCSV(InputStream fileInputStream, UUID courseId) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(fileInputStream))) {
            String[] line;
            Map<String, Discipline> disciplineMap = new HashMap<>();
            List<DisciplineTO> disciplineTOs = new ArrayList<>();

            boolean isHeader = true; // Ignorar a primeira linha (cabeçalho)
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                Integer semester = Integer.parseInt(line[0].trim());
                String code = line[1].trim();
                String name = line[2].trim();
                Integer workload = Integer.parseInt(line[3].trim());
                Integer credits = Integer.parseInt(line[4].trim());
                String nature = line[5].trim();
                String prerequisites = line.length > 6 ? line[6].trim() : "";

                DisciplineTO disciplineTO = new DisciplineTO();
                disciplineTO.setSemester(semester);
                disciplineTO.setDisciplineCode(code);
                disciplineTO.setName(name);
                disciplineTO.setWorkload(workload);
                disciplineTO.setClassCredits(credits);
                disciplineTO.setTypeOfDiscipline(mapNatureToType(nature));
                disciplineTO.setCourseId(courseId);
                disciplineTO.setPrerequisiteIds(resolvePrerequisites(prerequisites, disciplineMap));

                disciplineTOs.add(disciplineTO);
            }

            disciplineTOs.forEach(disciplineServicePort::save);
        }
    }

    private TypeOfDiscipline mapNatureToType(String nature) {
        if (nature == null || nature.isBlank()) {
            throw new IllegalArgumentException("Natureza da disciplina não pode ser nula ou vazia.");
        }

        switch (nature.trim().toUpperCase()) {
            case "OBRIGATÓRIA":
                return TypeOfDiscipline.OBRIGATORIA;
            case "ELETIVA":
                return TypeOfDiscipline.ELETIVA;
            case "OPTATIVA":
                return TypeOfDiscipline.OPTATIVA;
            default:
                throw new IllegalArgumentException("Natureza desconhecida: " + nature);
        }
    }


    private Set<UUID> resolvePrerequisites(String prerequisites, Map<String, Discipline> disciplineMap) {
        if (prerequisites == null || prerequisites.isBlank() || prerequisites.equalsIgnoreCase("sem pré-requisito")) {
            return Collections.emptySet();
        }

        Set<UUID> prerequisiteIds = new HashSet<>();
        String[] parts = prerequisites.split("\\|");

        for (String part : parts) {
            String code = part.split("–")[0].trim();
            Discipline discipline = disciplineMap.get(code);
            if (discipline != null) {
                prerequisiteIds.add(discipline.getId());
            }
        }
        return prerequisiteIds;
    }
}
