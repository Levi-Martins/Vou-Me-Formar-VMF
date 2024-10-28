package com.smd.ufccursos.application.exceptions;

import com.smd.ufccursos.domain.exceptions.ObjectNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationException(MethodArgumentNotValidException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var errorMessages = new ArrayList<String>();
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        logger.warn("Validation failed: ", ex);
        fieldErrors.forEach(e -> {
            var msgError = String.format("Field '%s' %s. Provided value: %s", e.getField(), e.getDefaultMessage(), e.getRejectedValue());
            errorMessages.add(msgError);
        });
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), errorMessages));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErroResponse> handleObjectNotFoundException(ObjectNotFoundException ex) {
        var status = HttpStatus.NOT_FOUND;
        logger.error("ObjectNotFoundException: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), ex.getMessage()));
    }

    //TODO: CONFERIR
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleObjectNotFoundException(BadCredentialsException ex) {
        var status = HttpStatus.FORBIDDEN;
        logger.error("BadCredentialsException: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("Unexpected error: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), "An unexpected error occurred. Please try again later."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.error("HttpMessageNotReadableException: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), "Invalid request body format."));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        var status = HttpStatus.METHOD_NOT_ALLOWED;
        String errorMessage = "Method " + ex.getMethod() + " not supported for this endpoint.";
        logger.error("HttpRequestMethodNotSupportedException: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .toList();
        logger.error("ConstraintViolationException: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), errorMessages));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        var status = HttpStatus.CONFLICT;
        String errorMessage = "Data integrity violation: " + ex.getMostSpecificCause().getMessage();
        logger.error("DataIntegrityViolationException: ", ex);
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), errorMessage));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErroResponse> handleTransactionSystemException(TransactionSystemException ex) {
        var status = HttpStatus.BAD_REQUEST;
        String errorMessage = "Transaction error occurred.";

        logger.error("TransactionSystemException: ", ex);

        if (ex.getRootCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
            List<String> errorMessages = constraintViolationException.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .toList();
            return ResponseEntity.status(status).body(new ErroResponse(status.value(), errorMessages));
        }

        if (ex.getRootCause() instanceof DataIntegrityViolationException) {
            errorMessage = "Data integrity violation error.";
        }

        return ResponseEntity.status(status).body(new ErroResponse(status.value(), errorMessage));
    }

}
