package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.PhoneTO;
import com.smd.ufccursos.domain.DTO.PhoneWithStudentTO;
import com.smd.ufccursos.domain.entity.Phone;
import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.PhoneRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.PhoneServicePort;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;

import java.util.Optional;
import java.util.UUID;

public class PhoneService implements PhoneServicePort {

    private final PhoneRepositoryPort phoneRepositoryPort;
    private final StudentServicePort studentServicePort;

    public PhoneService(PhoneRepositoryPort phoneRepositoryPort, StudentServicePort studentServicePort) {
        this.phoneRepositoryPort = phoneRepositoryPort;
        this.studentServicePort = studentServicePort;
    }

    @Override
    public PageTO<Phone> findAll(PaginationTO paginationTO) {
        return phoneRepositoryPort.findAll(paginationTO);
    }

    @Override
    public Phone findById(UUID id) {
        Optional<Phone> phone = phoneRepositoryPort.findById(id);
        if (phone.isEmpty()) {
            throw new RuntimeException("Phone not found");
        }
        return phone.get();
    }

    @Override
    public Phone save(PhoneWithStudentTO phoneWithStudentTO) {
        Student student = studentServicePort.findById(phoneWithStudentTO.getStudentId());

        Phone phone = Phone.builder()
                .ddd(phoneWithStudentTO.getDdd())
                .number(phoneWithStudentTO.getNumber())
                .student(student)
                .build();

        return phoneRepositoryPort.save(phone);
    }

    @Override
    public Phone update(UUID id, PhoneTO phoneTO) {
        Phone phoneToUpdate = findById(id);
        phoneToUpdate.setDdd(phoneTO.getDdd());
        phoneToUpdate.setNumber(phoneTO.getNumber());
        return phoneRepositoryPort.save(phoneToUpdate);
    }


    @Override
    public void deleteById(UUID id) {
        phoneRepositoryPort.deleteById(id);
    }
}
