package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.service.CSVImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/disciplineCSV")
public class DisciplineImportController {

    private final CSVImportService csvImportService;

    public DisciplineImportController(CSVImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file,
                                            @RequestParam("courseId") UUID courseId) throws Exception {

        csvImportService.importFromAnyFile(file, courseId);

        return ResponseEntity.ok("Importação realizada com sucesso!");
    }
}
