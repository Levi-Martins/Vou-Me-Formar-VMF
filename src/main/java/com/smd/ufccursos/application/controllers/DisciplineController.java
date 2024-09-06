package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.entity.Discipline;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("discipline")
public class DisciplineController {

    private final DisciplineServicePort disciplineServicePort;

    public DisciplineController(DisciplineServicePort disciplineServicePort) {
        this.disciplineServicePort = disciplineServicePort;
    }


    @PostMapping
    public Discipline create(@RequestBody Discipline discipline){
        return  disciplineServicePort.createDiscipline(discipline);
    }
}
