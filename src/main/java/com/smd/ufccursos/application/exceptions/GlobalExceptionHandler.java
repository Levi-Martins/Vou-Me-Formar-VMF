package com.smd.ufccursos.application.exceptions;

import com.smd.ufccursos.domain.exceptions.BusinessRuleException;
import com.smd.ufccursos.domain.exceptions.CSVImportValidationException;
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

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationException(MethodArgumentNotValidException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.warn("Validation failed: ", ex);

        List<ValidationErrorDetail> detalhesDosErros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()
                ))
                .collect(Collectors.toList());

        String erroTitulo = "Um ou mais campos estão inválidos";
        var erroResponse = new ErroResponse(status.value(), erroTitulo, detalhesDosErros);

        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErroResponse> handleObjectNotFoundException(ObjectNotFoundException ex) {
        var status = HttpStatus.NOT_FOUND;
        logger.error("ObjectNotFoundException: ", ex);
        var erroResponse = new ErroResponse(status.value(), "Objeto não encontrado", ex.getMessage());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleObjectNotFoundException(BadCredentialsException ex) {
        var status = HttpStatus.FORBIDDEN;
        logger.error("BadCredentialsException: ", ex);
        var erroResponse = new ErroResponse(status.value(), "Credenciais inválidas", ex.getMessage());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("Unexpected error: ", ex);
        String mensagem = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
        var erroResponse = new ErroResponse(status.value(), "Erro Interno", mensagem);
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.error("HttpMessageNotReadableException: ", ex);
        String mensagem = "Formato de requisição inválido ou ilegível.";
        var erroResponse = new ErroResponse(status.value(), "Requisição Inválida", mensagem);
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        var status = HttpStatus.METHOD_NOT_ALLOWED;
        String errorMessage = "Método " + ex.getMethod() + " não suportado para este endpoint.";
        logger.error("HttpRequestMethodNotSupportedException: ", ex);
        var erroResponse = new ErroResponse(status.value(), "Método não permitido", errorMessage);
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.error("ConstraintViolationException: ", ex);

        List<ValidationErrorDetail> detalhesDosErros = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationErrorDetail(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation.getInvalidValue()
                ))
                .collect(Collectors.toList());

        String erroTitulo = "Violação de constraint";
        var erroResponse = new ErroResponse(status.value(), erroTitulo, detalhesDosErros);
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        var status = HttpStatus.CONFLICT;
        String errorMessage = "Violação de integridade de dados: " + ex.getMostSpecificCause().getMessage();
        logger.error("DataIntegrityViolationException: ", ex);
        var erroResponse = new ErroResponse(status.value(), "Conflito de Dados", errorMessage);
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErroResponse> handleTransactionSystemException(TransactionSystemException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.error("TransactionSystemException capturada: ", ex);

        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof ConstraintViolationException constraintEx) {
            List<ValidationErrorDetail> detalhesDosErros = constraintEx.getConstraintViolations().stream()
                    .map(violation -> new ValidationErrorDetail(
                            violation.getPropertyPath().toString(),
                            violation.getMessage(),
                            violation.getInvalidValue()
                    ))
                    .collect(Collectors.toList());

            String erroTitulo = "Erro de validação nos dados enviados";
            var erroResponse = new ErroResponse(status.value(), erroTitulo, detalhesDosErros);
            return ResponseEntity.status(status).body(erroResponse);
        }

        var erroResponse = new ErroResponse(
                status.value(),
                "Erro de Transação",
                "Ocorreu um erro ao processar a transação. Verifique os dados e tente novamente."
        );
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErroResponse> handleBusinessRuleException(BusinessRuleException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.warn("BusinessRuleException: ", ex);
        var erroResponse = new ErroResponse(status.value(), "Regra de Negócio Violada", ex.getMessage());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(CSVImportValidationException.class)
    public ResponseEntity<ErroResponse> handleCSVImportValidationException(CSVImportValidationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        logger.warn("CSVImportValidationException: ", ex);

        var erroResponse = new ErroResponse(
                status.value(),
                "Erro de validação na planilha importada",
                ex.getFieldErrors()
        );

        return ResponseEntity.status(status).body(erroResponse);
    }
}


