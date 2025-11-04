package com.smd.ufccursos.domain.exceptions;

import com.smd.ufccursos.application.exceptions.ValidationErrorDetail;
import java.util.List;

public class CSVImportValidationException extends RuntimeException {
    private final List<ValidationErrorDetail> fieldErrors;

    public CSVImportValidationException(String message, List<ValidationErrorDetail> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public List<ValidationErrorDetail> getFieldErrors() {
        return fieldErrors;
    }
}
