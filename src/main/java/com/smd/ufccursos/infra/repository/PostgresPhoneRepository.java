package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Phone;
import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.PhoneRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresPhoneRepository implements PhoneRepositoryPort {

    private final SpringDataPhoneRepository springDataPhoneRepository;

    public PostgresPhoneRepository(SpringDataPhoneRepository springDataPhoneRepository) {
        this.springDataPhoneRepository = springDataPhoneRepository;
    }

    @Override
    public PageTO<Phone> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Page<Phone> page = springDataPhoneRepository.findAll(pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());    }

    @Override
    public Phone save(Phone entity) {
        return springDataPhoneRepository.save(entity);
    }

    @Override
    public Optional<Phone> findById(UUID id) {
        return springDataPhoneRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataPhoneRepository.deleteById(id);
    }
}
