package com.smd.ufccursos.application.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponse {
    private Integer status;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private List<String> erroMessages;

    public  ErroResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErroResponse(Integer status, List<String> erroMessages) {
        this.status = status;
        this.erroMessages = erroMessages;
    }
}
