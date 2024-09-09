package com.smd.ufccursos.domain.ports.repositoryPort;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;

import java.util.Optional;
import java.util.UUID;

public interface BaseRepositoryPort<T> {

    PageTO<T> findAll(PaginationTO paginationTO);

    T save(T entity);

    Optional<T> findById(UUID id);

    void deleteById(UUID id);
}
