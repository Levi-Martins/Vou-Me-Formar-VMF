package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.request.DisciplineDTORequest;
import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.response.DisciplineResponseDTO;
import com.smd.ufccursos.domain.entity.TypeOfDiscipline;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("discipline")
public class DisciplineController {

    private final DisciplineServicePort disciplineServicePort;

    public DisciplineController(DisciplineServicePort disciplineServicePort) {
        this.disciplineServicePort = disciplineServicePort;
    }

    @Operation(summary = "Página com 10 disciplinas")
    @GetMapping
    public ResponseEntity<PageTO<DisciplineResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @Parameter(description = "Tipo da disciplina")
            @RequestParam(required = false) TypeOfDiscipline typeOfDiscipline,
            @RequestParam(required = false) Integer workload,
            @RequestParam(required = false) Integer classCredits,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) Boolean hasPrerequisites,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PaginationTO paginationTO = new PaginationTO(page, size);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("typeOfDiscipline", typeOfDiscipline);
        params.put("workload", workload);
        params.put("classCredits", classCredits);
        params.put("semester", semester);
        params.put("courseId", courseId != null ? UUID.fromString(courseId) : null);
        params.put("hasPrerequisites", hasPrerequisites);
        paginationTO.setParams(params);
        return ResponseEntity.ok(disciplineServicePort.findAll(paginationTO));
    }


    @Operation(summary = "Chamar disciplina")
    @GetMapping(value = "/{id}")
    public ResponseEntity<DisciplineResponseDTO> findById(@PathVariable UUID id){
        return ResponseEntity.ok().body(disciplineServicePort.findById(id));
    }


    @Operation(summary = "Criar disciplina")
    @PostMapping
    public ResponseEntity<DisciplineResponseDTO> create(@Valid @RequestBody DisciplineDTORequest disciplineDTORequest){
        return new ResponseEntity<>(disciplineServicePort.save(disciplineDTORequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar disciplina")
    @PutMapping(value = "/{id}")
    public ResponseEntity<DisciplineResponseDTO> update(@PathVariable UUID id, @RequestBody DisciplineDTORequest disciplineDTORequest) {
        return ResponseEntity.ok().body(disciplineServicePort.update(id, disciplineDTORequest));
    }

    @Operation(summary = "Deletar disciplina")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        disciplineServicePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
