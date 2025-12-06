package com.smd.ufccursos.infra.client;

import com.smd.ufccursos.domain.DTO.response.PythonPdfResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class PythonPdfClient {

    private final WebClient webClient;

    public PythonPdfClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public PythonPdfResponseDTO extractDataFromPDF (MultipartFile pdf){
        try{
            return webClient.post()
                    .uri("/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", pdf.getResource()))
                    .retrieve()
                    .bodyToMono(PythonPdfResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e){
            throw new RuntimeException("Erro ao chamar serviço Python: " + e.getResponseBodyAsString(), e);
        }
    }


}
