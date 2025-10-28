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
    private String erro; // Um título geral para o erro (ex: "Validação falhou")
    private String mensagem; // Para erros simples (ex: "Objeto não encontrado")
    private List<DetalheErroValidacao> detalhes; // << NOSSA MUDANÇA PRINCIPAL

    /**
     * Construtor para erros simples (a maioria dos seus handlers)
     */
    public ErroResponse(Integer status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
    }

    /**
     * Construtor para erros de validação (com a lista de detalhes)
     */
    public ErroResponse(Integer status, String erro, List<DetalheErroValidacao> detalhes) {
        this.status = status;
        this.erro = erro;
        this.detalhes = detalhes;
    }
}