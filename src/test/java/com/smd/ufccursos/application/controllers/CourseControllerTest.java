package com.smd.ufccursos.application.controllers;

import com.smd.ufccursos.domain.DTO.request.CourseDTORequest;
import com.smd.ufccursos.domain.DTO.response.CourseDTOResponse;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseServicePort courseServicePort;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    @DisplayName("[CT-06] - Deve retornar Bad Request (400) quando horas obrigatórias receberem texto (não numérico)")
    void shouldReturnBadRequest_WhenMandatoryHoursIsNotNumber() throws Exception {
        // 1. ARRANGE
        String jsonRequestInvalido = """
            {
                "name": "Engenharia de Software",
                "department": "Computação",
                "requirements": {
                    "requiredMandatoryHours": "texto-invalido", 
                    "requiredOptionalHours": 300,
                    "requiredComplementaryHours": 100
                }
            }
        """;

        // 2. ACT & ASSERT
        mockMvc.perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestInvalido))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Sucesso] - Deve criar curso quando todos os dados forem válidos")
    void shouldCreateCourseSuccessfully() throws Exception {
        // 1. ARRANGE
        String jsonRequestValido = """
            {
                "name": "Engenharia de Software",
                "department": "Computação",
                "requirements": {
                    "requiredMandatoryHours": 3000,
                    "requiredOptionalHours": 300,
                    "requiredComplementaryHours": 100
                }
            }
        """;

        CourseDTOResponse responseMock = new CourseDTOResponse();
        responseMock.setName("Engenharia de Software");

        when(courseServicePort.save(any(CourseDTORequest.class))).thenReturn(responseMock);

        // 2. ACT & ASSERT
        mockMvc.perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestValido))
                .andExpect(status().isCreated());
    }
}