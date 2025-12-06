package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PdfGraduationOrchestratorPort {
    GraduationCheckResponse checkGraduationByPdf(UUID courseId, MultipartFile pdf);
}
