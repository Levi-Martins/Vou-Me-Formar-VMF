package com.smd.ufccursos.domain.ports.servicePort;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.request.StudentTO;
import com.smd.ufccursos.domain.entity.Student;

import java.util.UUID;

public interface StudentServicePort {
    PageTO<Student> findAll(PaginationTO paginationTO);
    Student findById(UUID id);
    Student save(StudentTO studentTO);
    Student update(UUID id, StudentTO studentTO);
    void deleteById(UUID id);
}
