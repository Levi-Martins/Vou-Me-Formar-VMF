package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.service.CSVImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("disciplineCSV")
public class DisciplineImportController {

    private final CSVImportService csvImportService;
    public DisciplineImportController(CSVImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file,
                                            @RequestParam("courseId") UUID courseId) {
        try (InputStream inputStream = file.getInputStream()) {
            csvImportService.importFromCSV(inputStream, courseId);
            return ResponseEntity.ok("Importação realizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao importar o arquivo CSV: " + e.getMessage());
        }
    }


}
