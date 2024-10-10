package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("course")
public class CourseController {

    private final CourseServicePort courseServicePort;

    public CourseController(CourseServicePort courseServicePort) {
        this.courseServicePort = courseServicePort;
    }

    @Operation(summary = "Página com 10 cursos")
    @GetMapping
    public ResponseEntity<PageTO<Course>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PaginationTO paginationTO = new PaginationTO(page, size);
        Map<String, Object> params = new HashMap<>();
        paginationTO.setParams(params);
        return ResponseEntity.ok(courseServicePort.findAll(paginationTO));
    }

    @Operation(summary = "Chamar curso")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Course> findById(@PathVariable UUID id){
        return ResponseEntity.ok().body(courseServicePort.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar curso")
    public ResponseEntity<Course> create(@RequestBody @Valid Course course) {
        return new ResponseEntity<>(courseServicePort.save(course), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar curso")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Course> update(@PathVariable UUID id, @RequestBody Course course) {
        return ResponseEntity.ok().body(courseServicePort.update(id, course));
    }

    @Operation(summary = "Deletar curso")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        courseServicePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
