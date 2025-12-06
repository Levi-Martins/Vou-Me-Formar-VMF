package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;
import com.smd.ufccursos.domain.DTO.response.PythonPdfResponseDTO;
import com.smd.ufccursos.domain.mapper.PdfGraduationCheckMapper;
import com.smd.ufccursos.domain.ports.servicePort.GraduationCheckServicePort;
import com.smd.ufccursos.domain.ports.servicePort.PdfGraduationOrchestratorPort;
import com.smd.ufccursos.infra.client.PythonPdfClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class PdfGraduationOrchestratorService implements PdfGraduationOrchestratorPort {
    private final PythonPdfClient pythonClient;
    private final PdfGraduationCheckMapper mapper;
    private final GraduationCheckServicePort graduationCheckService; // Seu serviço existente

    public PdfGraduationOrchestratorService(PythonPdfClient pythonClient, PdfGraduationCheckMapper mapper, GraduationCheckServicePort graduationCheckService) {
        this.pythonClient = pythonClient;
        this.mapper = mapper;
        this.graduationCheckService = graduationCheckService;
    }

    @Override
    public GraduationCheckResponse checkGraduationByPdf(UUID courseId, MultipartFile pdf) {
        // 1. Chamar o Python
        PythonPdfResponseDTO pythonData = pythonClient.extractDataFromPDF(pdf);

        // 2. Mapear a resposta para o DTO do seu serviço
        GraduationCheckRequest graduationCheckRequest = mapper.toGraduationCheckRequest(pythonData, courseId);

        // 3. Chamar seu serviço principal com os dados traduzidos
        return graduationCheckService.checkGraduationEligibility(graduationCheckRequest);
    }
}
