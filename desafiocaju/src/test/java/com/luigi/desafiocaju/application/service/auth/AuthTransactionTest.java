package com.luigi.desafiocaju.application.service.auth;

import com.luigi.desafiocaju.application.dto.TransactionInDTO;
import com.luigi.desafiocaju.application.dto.TransactionOutDTO;
import com.luigi.desafiocaju.application.service.BalanceService;
import com.luigi.desafiocaju.application.service.CategoryService;
import com.luigi.desafiocaju.domain.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class AuthTransactionTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private AuthTransaction authTransaction;

    @BeforeEach
    void setup() {
        authTransaction = new AuthTransaction(categoryService, balanceService);
    }


    @Test
    void testTransactionApproved() {
        TransactionInDTO inDTO = new TransactionInDTO(
                1L, "5412", new BigDecimal("2000.00"), " Merchant");

        when(categoryService.findCategoriesWithFallback(inDTO.merchant(), inDTO.mcc()))
                .thenReturn(Set.of(Category.FOOD));

        when(balanceService.debitTransaction(anyLong(), any(Category.class), any(BigDecimal.class)))
                .thenReturn(true);

        TransactionOutDTO result = authTransaction.execute(inDTO);

        assertEquals(TransactionOutDTO.approved(), result, "Transaction should be approved when BALANCE is sufficient");
    }

    @Test
    void testTransactionInsufficientFunds() {
        TransactionInDTO input = new TransactionInDTO(
                1L,"5412", new BigDecimal("2000.00"), "Merchant");

        when(categoryService.findCategoriesWithFallback(input.merchant(), input.mcc()))
                .thenReturn(Set.of(Category.FOOD));

        when(balanceService.debitTransaction(anyLong(), any(Category.class), any(BigDecimal.class)))
                .thenReturn(false);

        TransactionOutDTO result = authTransaction.execute(input);

        assertEquals(TransactionOutDTO.rejected(), result, "Transaction must be REJECTED due insufficient funds");
    }

    @Test
    void testTransactionFailed() {
        TransactionInDTO input = new TransactionInDTO(
                1L, "5412", new BigDecimal("2000.00"), "Merchant");

        when(categoryService.findCategoriesWithFallback(input.merchant(), input.mcc()))
                .thenThrow(new RuntimeException("UNEXPECTED ERROR DURING TRANSACTION"));

        TransactionOutDTO result = authTransaction.execute(input);

        assertEquals(TransactionOutDTO.failed(), result, "Transaction must be FAILED due to an unexpected error");
    }
}