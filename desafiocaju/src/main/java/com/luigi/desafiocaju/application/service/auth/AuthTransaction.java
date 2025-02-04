package com.luigi.desafiocaju.application.service.auth;

import com.luigi.desafiocaju.application.dto.TransactionInDTO;
import com.luigi.desafiocaju.application.dto.TransactionOutDTO;
import com.luigi.desafiocaju.application.service.BalanceService;
import com.luigi.desafiocaju.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthTransaction {

    CategoryService categoryService;
    BalanceService balanceService;

    @Autowired
    public AuthTransaction(CategoryService categoryService, BalanceService balanceService) {
        this.categoryService = categoryService;
        this.balanceService = balanceService;
    }

    public TransactionOutDTO execute(TransactionInDTO inDTO) {
        try {
            boolean isApproved = categoryService
                    .findCategoriesWithFallback(inDTO.merchant(), inDTO.mcc()).stream()
                    .anyMatch(category -> balanceService
                            .debitTransaction(inDTO.accountId(), category, inDTO.amount()));

            return isApproved ? TransactionOutDTO.approved() : TransactionOutDTO.rejected();
        } catch (Exception e) {
            return TransactionOutDTO.failed();
        }
    }


}
