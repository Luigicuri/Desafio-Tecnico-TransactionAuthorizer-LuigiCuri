package com.luigi.desafiocaju.application.service;

import com.luigi.desafiocaju.domain.entity.Account;
import com.luigi.desafiocaju.domain.entity.Balance;
import com.luigi.desafiocaju.domain.entity.Category;
import com.luigi.desafiocaju.domain.exception.ErrorUpdateBalanceException;
import com.luigi.desafiocaju.domain.repository.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    private final Account account = new Account(1L, "Account");
    private final Balance balance = new Balance(1L, account, new BigDecimal("500.00"), Category.MEAL);

    @Test
    void testAttemptDebitWithRegularBalance() {

        Balance sufficientBalance = new Balance(1L, account, new BigDecimal("500.00"), Category.MEAL); // 500 available

        lenient().when(balanceRepository.findByAccount_IdAndCategory(anyLong(), any(Category.class)))
                .thenReturn(Optional.of(sufficientBalance));

        boolean transactionResult = balanceService.debitTransaction(1L, Category.MEAL, new BigDecimal("200.00"));


        assertTrue(transactionResult, "Transaction should be approved with sufficient funds");

        assertEquals(new BigDecimal("300.00"), sufficientBalance.getTotalAmount(), "Balance should be updated correctly after the transaction");
    }

    @Test
    void testAttemptDebit_BalanceNotFound() {
        when(balanceRepository.findByAccount_IdAndCategory(anyLong(), any(Category.class)))
                .thenReturn(Optional.empty());

        boolean result = balanceService.debitTransaction(1L, Category.MEAL, new BigDecimal("200.00"));

        assertFalse(result, "Debit should not be allowed when balance is not found");

    }

}
