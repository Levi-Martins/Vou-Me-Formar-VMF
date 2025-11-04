package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.request.DisciplineDTORequest;
import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.response.DisciplineResponseDTO;
import com.smd.ufccursos.domain.entity.Discipline;

import java.util.UUID;

public interface DisciplineServicePort {

    PageTO<DisciplineResponseDTO> findAll(PaginationTO paginationTO);
    DisciplineResponseDTO findById(UUID id);
    DisciplineResponseDTO save(DisciplineDTORequest disciplineDTORequest);
    DisciplineResponseDTO update(UUID id, DisciplineDTORequest disciplineDTORequest);
    void deleteById(UUID id);
    Discipline findByDisciplineCode(String code);
}
