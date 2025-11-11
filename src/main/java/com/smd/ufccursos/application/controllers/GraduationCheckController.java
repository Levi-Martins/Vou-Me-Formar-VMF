package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;
import com.smd.ufccursos.domain.ports.servicePort.GraduationCheckServicePort;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("graduation-check")
public class GraduationCheckController {

    private final GraduationCheckServicePort graduationCheckServicePort;

    public GraduationCheckController(GraduationCheckServicePort graduationCheckServicePort) {
        this.graduationCheckServicePort = graduationCheckServicePort;
    }

    @Operation(summary = "Validar se aluno está apto a se formar")
    @PostMapping
    public ResponseEntity<GraduationCheckResponse> graduationCheck(@RequestBody GraduationCheckRequest graduationCheckRequest) {
        return new ResponseEntity<>(graduationCheckServicePort.checkGraduationEligibility(graduationCheckRequest), HttpStatus.CREATED);
    }
}
