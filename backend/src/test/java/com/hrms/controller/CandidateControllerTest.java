package com.hrms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.dto.CandidateDTO;
import com.hrms.entity.MilitaryStatus;
import com.hrms.service.CandidateService;
import com.hrms.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidateController.class)
@DisplayName("Candidate Controller Tests")
class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    private CandidateDTO testCandidateDTO;

    @BeforeEach
    void setUp() {
        testCandidateDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("5551234567")
                .position("Developer")
                .militaryStatus(MilitaryStatus.COMPLETED)
                .noticePeriodDays(30)
                .build();
    }

    @Test
    @DisplayName("GET /api/candidates should return list of all candidates")
    void getAll() throws Exception {
        when(candidateService.getAllCandidates())
                .thenReturn(Arrays.asList(testCandidateDTO));

        mockMvc.perform(get("/api/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    @DisplayName("PUT /api/candidates/{id} should update candidate")
    void update() throws Exception {
        when(candidateService.updateCandidate(eq(1L), any(CandidateDTO.class)))
                .thenReturn(testCandidateDTO);

        mockMvc.perform(put("/api/candidates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCandidateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("DELETE /api/candidates/{id} should remove candidate")
    void deleteCandidate() throws Exception {
        mockMvc.perform(delete("/api/candidates/1"))
                .andExpect(status().isNoContent());
    }
} 