package com.banking.deposit.presentation.controller;

import com.banking.deposit.application.dto.DepositRequest;
import com.banking.deposit.application.dto.DepositResponse;
import com.banking.deposit.application.service.DepositService;
import com.banking.deposit.domain.model.DepositStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepositController.class)
class DepositControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private DepositService depositService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private DepositResponse createDepositResponse() {
        DepositResponse response = new DepositResponse();
        response.setId(1L);
        response.setAccountNumber("ACC123456789");
        response.setAmount(new BigDecimal("1000.00"));
        response.setStatus(DepositStatus.PENDING);
        response.setCurrency("USD");
        response.setDescription("Test deposit");
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());
        return response;
    }
    
    @Test
    void testCreateDeposit() throws Exception {
        DepositRequest request = DepositRequest.builder()
                .accountNumber("ACC123456789")
                .amount(new BigDecimal("1000.00"))
                .currency("USD")
                .description("Test deposit")
                .build();
        
        DepositResponse response = createDepositResponse();
        
        when(depositService.createDeposit(any(DepositRequest.class))).thenReturn(response);
        
        mockMvc.perform(post("/api/v1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456789"))
                .andExpect(jsonPath("$.amount").value(1000.00));
    }
    
    @Test
    void testGetDepositById() throws Exception {
        DepositResponse response = createDepositResponse();
        
        when(depositService.getDepositById(1L)).thenReturn(response);
        
        mockMvc.perform(get("/api/v1/deposits/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456789"));
    }
    
    @Test
    void testGetAllDeposits() throws Exception {
        List<DepositResponse> deposits = Arrays.asList(createDepositResponse());
        Page<DepositResponse> page = new PageImpl<>(deposits);
        
        when(depositService.getAllDeposits(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);
        
        mockMvc.perform(get("/api/v1/deposits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
    
    @Test
    void testUpdateDepositStatus() throws Exception {
        DepositResponse response = createDepositResponse();
        response.setStatus(DepositStatus.COMPLETED);
        
        when(depositService.updateDepositStatus(eq(1L), eq(DepositStatus.COMPLETED)))
                .thenReturn(response);
        
        mockMvc.perform(patch("/api/v1/deposits/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
    
    @Test
    void testDeleteDeposit() throws Exception {
        mockMvc.perform(delete("/api/v1/deposits/1"))
                .andExpect(status().isNoContent());
    }
}

