package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.request.GraduationCheckRequest;
import com.smd.ufccursos.domain.DTO.response.GraduationCheckResponse;

public interface GraduationCheckServicePort {

    GraduationCheckResponse checkGraduationEligibility(GraduationCheckRequest graduationCheckRequest);
}
