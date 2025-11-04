package com.smd.ufccursos.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationErrorDetail {
    private String field;
    private String message;
    private Object Rejectedvalue;
}
