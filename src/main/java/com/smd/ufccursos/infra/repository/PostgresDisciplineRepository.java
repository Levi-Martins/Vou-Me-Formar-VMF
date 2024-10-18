package com.smd.ufccursos.infra.repository;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostgresDisciplineRepository implements DisciplineRepositoryPort {

    private final SpringDataDisciplineRepository springDataDisciplineRepository;

    public PostgresDisciplineRepository(SpringDataDisciplineRepository springDataDisciplineRepository) {
        this.springDataDisciplineRepository = springDataDisciplineRepository;
    }

    @Override
    public PageTO<Discipline> findAll(PaginationTO paginationTO) {
        Pageable pageable = Pageable.ofSize(paginationTO.getSize()).withPage(paginationTO.getPage());
        Specification<Discipline> spec = getFilteredDisciplines(paginationTO.getParams());
        Page<Discipline> page = springDataDisciplineRepository.findAll(spec,pageable);
        return new PageTO<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    private Specification<Discipline> getFilteredDisciplines(Map<String, Object> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("name") && params.get("name") != null) {
                String name = ((String) params.get("name")).toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name + "%"));
            }

            if (params.containsKey("typeOfDiscipline") && params.get("typeOfDiscipline") != null) {
                TypeOfDiscipline type = (TypeOfDiscipline) params.get("typeOfDiscipline");
                predicates.add(cb.equal(root.get("typeOfDiscipline"), type));
            }

            if (params.containsKey("workload") && params.get("workload") != null) {
                Integer workload = (Integer) params.get("workload");
                predicates.add(cb.equal(root.get("workload"), workload));
            }

            if (params.containsKey("classCredits") && params.get("classCredits") != null) {
                Integer credits = (Integer) params.get("classCredits");
                predicates.add(cb.equal(root.get("classCredits"), credits));
            }

            if (params.containsKey("semester") && params.get("semester") != null) {
                Integer semester = (Integer) params.get("semester");
                predicates.add(cb.equal(root.get("semester"), semester));
            }

            if (params.containsKey("courseId") && params.get("courseId") != null) {
                UUID courseId = (UUID) params.get("courseId");
                predicates.add(cb.equal(root.get("course").get("id"), courseId));
            }

            if (params.containsKey("hasPrerequisites") && params.get("hasPrerequisites") != null) {
                boolean hasPrerequisites = (Boolean) params.get("hasPrerequisites");
                if (hasPrerequisites) {
                    predicates.add(cb.isNotEmpty(root.get("prerequisites")));
                } else {
                    predicates.add(cb.isEmpty(root.get("prerequisites")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Override
    public Discipline save(Discipline discipline) {
        return springDataDisciplineRepository.save(discipline);
    }

    @Override
    public Optional<Discipline> findById(UUID id) {
        return springDataDisciplineRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataDisciplineRepository.deleteById(id);
    }
}
