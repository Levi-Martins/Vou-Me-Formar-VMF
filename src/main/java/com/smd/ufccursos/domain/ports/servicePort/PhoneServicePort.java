package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.*;
import com.smd.ufccursos.domain.DTO.request.PhoneTO;
import com.smd.ufccursos.domain.DTO.request.PhoneWithStudentTO;
import com.smd.ufccursos.domain.entity.Phone;

import java.util.UUID;

public interface PhoneServicePort {
    PageTO<Phone> findAll(PaginationTO paginationTO);
    Phone findById(UUID id);
    Phone save(PhoneWithStudentTO phoneWithStudentTO);
    Phone update(UUID id, PhoneTO phoneTO);
    void deleteById(UUID id);
}
