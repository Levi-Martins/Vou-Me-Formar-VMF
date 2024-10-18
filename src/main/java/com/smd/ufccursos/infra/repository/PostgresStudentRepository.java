package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class PostgresStudentRepository implements StudentRepositoryPort {

    private final SpringDataStudentRepository springDataStudentRepository;

    public PostgresStudentRepository(SpringDataStudentRepository springDataStudentRepository) {
        this.springDataStudentRepository = springDataStudentRepository;
    }

    @Override
    public PageTO<Student> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Specification<Student> spec = getFilteredStudents(paginationTO.getParams());
        Page<Student> page = springDataStudentRepository.findAll(spec, pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    private Specification<Student> getFilteredStudents(Map<String, Object> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("name") && params.get("name") != null) {
                String name = ((String) params.get("name")).toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name + "%"));
            }

            if (params.containsKey("email") && params.get("email") != null) {
                String email = ((String) params.get("email")).toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email + "%"));
            }

            if (params.containsKey("registration") && params.get("registration") != null) {
                Integer registration = (Integer) params.get("registration");
                predicates.add(cb.equal(root.get("registration"), registration));
            }

            if (params.containsKey("registrationDate") && params.get("registrationDate") != null) {
                LocalDate registrationDate = (LocalDate) params.get("registrationDate");
                predicates.add(cb.equal(root.get("registrationDate"), registrationDate));
            }

            if (params.containsKey("courseId") && params.get("courseId") != null) {
                UUID courseId = (UUID) params.get("courseId");
                predicates.add(cb.equal(root.get("course").get("id"), courseId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Override
    public Student save(Student entity) {
        return springDataStudentRepository.save(entity);
    }

    @Override
    public Optional<Student> findById(UUID id) {
        return springDataStudentRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataStudentRepository.deleteById(id);
    }
}
