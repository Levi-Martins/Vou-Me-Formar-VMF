package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;
import com.smd.ufccursos.domain.ports.servicePort.GraduationCheckServicePort;
import com.smd.ufccursos.domain.ports.servicePort.PdfGraduationOrchestratorPort;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("graduation-check")
public class GraduationCheckController {

    private final GraduationCheckServicePort graduationCheckServicePort;
    private final PdfGraduationOrchestratorPort pdfGraduationOrchestratorPort;

    public GraduationCheckController(GraduationCheckServicePort graduationCheckServicePort, PdfGraduationOrchestratorPort pdfGraduationOrchestratorPort) {
        this.graduationCheckServicePort = graduationCheckServicePort;
        this.pdfGraduationOrchestratorPort = pdfGraduationOrchestratorPort;
    }

    @Operation(summary = "Validar se aluno está apto a se formar")
    @PostMapping
    public ResponseEntity<GraduationCheckResponse> graduationCheck(@RequestBody GraduationCheckRequest graduationCheckRequest) {
        return new ResponseEntity<>(graduationCheckServicePort.checkGraduationEligibility(graduationCheckRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Validar aptidão de aluno por PDF do histórico")
    @PostMapping(value = "/upload-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GraduationCheckResponse> graduationCheckByPdf(
            @RequestParam("courseId") UUID courseId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        GraduationCheckResponse response = pdfGraduationOrchestratorPort.checkGraduationByPdf(courseId, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
