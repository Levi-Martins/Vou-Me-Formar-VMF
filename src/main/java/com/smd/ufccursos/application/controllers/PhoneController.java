package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.request.PhoneTO;
import com.smd.ufccursos.domain.DTO.request.PhoneWithStudentTO;
import com.smd.ufccursos.domain.entity.Phone;
import com.smd.ufccursos.domain.ports.servicePort.PhoneServicePort;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("phone")
public class PhoneController {

    private final PhoneServicePort phoneServicePort;

    public PhoneController(PhoneServicePort phoneServicePort) {
        this.phoneServicePort = phoneServicePort;
    }

    @Operation(summary = "Página com 10 telefones")
    @GetMapping
    public ResponseEntity<PageTO<Phone>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PaginationTO paginationTO = new PaginationTO(page, size);
        Map<String, Object> params = new HashMap<>();
        paginationTO.setParams(params);
        return ResponseEntity.ok(phoneServicePort.findAll(paginationTO));
    }

    @Operation(summary = "Chamar telefone")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Phone> findById(@PathVariable UUID id){
        return ResponseEntity.ok().body(phoneServicePort.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar telefone")
    public ResponseEntity<Phone> create(@RequestBody PhoneWithStudentTO phoneWithStudentTO) {
        return new ResponseEntity<>(phoneServicePort.save(phoneWithStudentTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar telefone")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Phone> update(@PathVariable UUID id, @RequestBody PhoneTO phoneTO) {
        return ResponseEntity.ok().body(phoneServicePort.update(id, phoneTO));
    }

    @Operation(summary = "Deletar telefone")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        phoneServicePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
