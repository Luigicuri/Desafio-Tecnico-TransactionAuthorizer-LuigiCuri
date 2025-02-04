package com.luigi.desafiocaju.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luigi.desafiocaju.application.dto.TransactionInDTO;
import com.luigi.desafiocaju.application.dto.TransactionOutDTO;
import com.luigi.desafiocaju.application.service.auth.AuthTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthTransaction authTransaction;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testAuthorizeTransactionSuccess() throws Exception {
        TransactionInDTO inDTO = new TransactionInDTO(1L, "5412", new BigDecimal("2000.00"), "Merchant");
        TransactionOutDTO outDTO = TransactionOutDTO.approved();

        when(authTransaction.execute(any(TransactionInDTO.class)))
                .thenReturn(outDTO);

        String json = objectMapper.writeValueAsString(inDTO);

        mockMvc.perform(post("/api/transactions/authTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkAuth").value(Boolean.TRUE))
                .andExpect(jsonPath("$.code").value("00"))
                .andExpect(jsonPath("$.message").value("APPROVED TRANSACTION"));
    }

    @Test
    void testAuthorizeTransactionInsufficientFunds() throws Exception {
        TransactionInDTO inDTO = new TransactionInDTO(1L, "5412", new BigDecimal("2000.00"), "Merchant");
        TransactionOutDTO outDTO = TransactionOutDTO.rejected();

        when(authTransaction.execute(any(TransactionInDTO.class)))
                .thenReturn(outDTO);

        String json = objectMapper.writeValueAsString(inDTO);

        mockMvc.perform(post("/api/transactions/authTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkAuth").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value("51"))
                .andExpect(jsonPath("$.message").value("INSUFFICIENT BALANCE TRANSACTION REJECTED"));
    }

    @Test
    void testAuthorizeTransactionFailedTransaction() throws Exception {
        TransactionInDTO inDTO = new TransactionInDTO(1L, "5412", new BigDecimal("2000.00"), "Merchant");
        TransactionOutDTO outDTO = TransactionOutDTO.failed();

        when(authTransaction.execute(any(TransactionInDTO.class)))
                .thenReturn(outDTO);

        String json = objectMapper.writeValueAsString(inDTO);

        mockMvc.perform(post("/api/transactions/authTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkAuth").value(Boolean.FALSE))
                .andExpect(jsonPath("$.code").value("07"))
                .andExpect(jsonPath("$.message").value("UNEXPECTED ERROR TRANSACTION FAILED"));
    }
}
