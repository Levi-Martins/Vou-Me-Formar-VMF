package com.smd.ufccursos.application.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Ótimo, mantenha isso!
public class ErroResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private Integer status;
    private String erro;
    private String mensagem;
    private List<ValidationErrorDetail> detalhes;


    public ErroResponse(Integer status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
    }

    public ErroResponse(Integer status, String erro, List<ValidationErrorDetail> detalhes) {
        this.status = status;
        this.erro = erro;
        this.detalhes = detalhes;
    }
}