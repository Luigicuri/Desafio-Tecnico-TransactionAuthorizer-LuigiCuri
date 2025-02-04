package com.luigi.desafiocaju.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luigi.desafiocaju.application.dto.TransactionInDTO;
import com.luigi.desafiocaju.domain.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AuthTransactionIntegrationTest {


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DirtiesContext
    @Sql("/testdb/populatedata-test.sql")
    void testAuthorizeTransaction_ShouldDeductAmountFromBalance() throws Exception {

        long accountId = 1L;
        String merchantName = "Test Merchant";
        String mcc = "5411";
        BigDecimal amount = new BigDecimal("50.00");

        TransactionInDTO input = new TransactionInDTO(accountId, mcc, amount, merchantName);
        String inputJson = objectMapper.writeValueAsString(input);

        mockMvc.perform(post("/api/transactions/authTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk());


        BigDecimal newBalanceAmount = getAmountByAccountAndCategory(accountId, Category.FOOD);

        BigDecimal expectedNewBalance = new BigDecimal("50.00");
        assertEquals(expectedNewBalance, newBalanceAmount, "The balance should be updated correctly.");
    }

    @Test
    @DirtiesContext
    @Sql("/testdb/populatedata-test.sql")
    void testAuthorizeTransaction_ShouldNotAlterBalance_WhenInsufficientFunds() throws Exception {
        long accountId = 1L;
        String merchantName = "Supermarket";
        String mcc = "5411";
        BigDecimal amount = new BigDecimal("150.00");

        TransactionInDTO request = new TransactionInDTO(accountId, mcc, amount, merchantName);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/transactions/authTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkAuth").value(false))
                .andExpect(jsonPath("$.code").value("51"))
                .andExpect(jsonPath("$.message").value("INSUFFICIENT BALANCE TRANSACTION REJECTED"));

        BigDecimal newBalanceAmount = getAmountByAccountAndCategory(accountId, Category.FOOD);
        BigDecimal expectedBalance = new BigDecimal("100.00");

        assertEquals(expectedBalance, newBalanceAmount, "Balance should remain unchanged due to insufficient funds.");
    }

    private BigDecimal getAmountByAccountAndCategory(long accountId, Category category) {
        return jdbcTemplate.queryForObject(
                "SELECT total_amount FROM balance WHERE account_id = ? AND category = ?",
                new Object[]{accountId, category.name()},
                BigDecimal.class
        );
    }
}
