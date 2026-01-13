package com.smd.ufccursos.infra.client;

import com.smd.ufccursos.domain.DTO.response.PythonPdfResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class PythonPdfClient {

    private final WebClient webClient;

    public PythonPdfClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public PythonPdfResponseDTO extractDataFromPDF(MultipartFile pdf) {
        try {
            return webClient.post()
                    .uri("/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", pdf.getResource()))
                    .retrieve()
                    .bodyToMono(PythonPdfResponseDTO.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                            .filter(throwable -> true)
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                    new RuntimeException("Serviço Python indisponível após 3 tentativas. O container pode estar falhando ao iniciar.")
                            ))
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar PDF no microsserviço: " + e.getMessage(), e);
        }
    }
}