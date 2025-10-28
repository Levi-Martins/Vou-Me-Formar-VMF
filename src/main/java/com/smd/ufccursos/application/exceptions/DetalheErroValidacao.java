package com.smd.ufccursos.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetalheErroValidacao {
    private String campo;
    private String mensagem;
    private Object valorRejeitado;
}
