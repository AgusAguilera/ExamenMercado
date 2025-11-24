package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.service.MutantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testMutantReturns200() throws Exception {
        // Simulamos que es mutante
        when(mutantService.analyzeDna(any())).thenReturn(true);

        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"AAAA", "CCCC", "TCAG", "GGTC"});

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testHumanReturns403() throws Exception {
        // Simulamos que NO es mutante
        when(mutantService.analyzeDna(any())).thenReturn(false);

        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"ATCG", "ATCG", "ATCG", "ATCG"});

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetStatsReturns200() throws Exception {
        // Simulamos una respuesta del servicio
        org.example.dto.StatsResponse mockStats = new org.example.dto.StatsResponse(10, 5, 2.0);
        when(mutantService.getStats()).thenReturn(mockStats);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}