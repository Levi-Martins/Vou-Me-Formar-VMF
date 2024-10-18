package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.request.StudentTO;
import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentServicePort studentServicePort;

    public StudentController(StudentServicePort studentServicePort) {
        this.studentServicePort = studentServicePort;
    }

    @Operation(summary = "Página com 10 estudantes")
    @GetMapping
    public ResponseEntity<PageTO<Student>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer registration,
            @RequestParam(required = false) LocalDate registrationDate,
            @RequestParam(required = false) UUID courseId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PaginationTO paginationTO = new PaginationTO(page, size);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("registration", registration);
        params.put("registrationDate", registrationDate);
        params.put("courseId", courseId);
        paginationTO.setParams(params);
        return ResponseEntity.ok(studentServicePort.findAll(paginationTO));
    }

    @Operation(summary = "Chamar estudante")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Student> findById(@PathVariable UUID id){
        return ResponseEntity.ok().body(studentServicePort.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar estudante")
    public ResponseEntity<Student> create(@RequestBody StudentTO studentTO) {
        return new ResponseEntity<>(studentServicePort.save(studentTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar estudante")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Student> update(@PathVariable UUID id, @RequestBody StudentTO studentTO) {
        return ResponseEntity.ok().body(studentServicePort.update(id, studentTO));
    }

    @Operation(summary = "Deletar estudante")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentServicePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
