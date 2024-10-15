package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.request.DisciplineTO;
import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Discipline;

import java.util.UUID;

public interface DisciplineServicePort {

    PageTO<Discipline> findAll(PaginationTO paginationTO);
    Discipline findById(UUID id);
    Discipline save(DisciplineTO disciplineTO);
    Discipline update(UUID id, DisciplineTO disciplineTO);
    void deleteById(UUID id);
}
