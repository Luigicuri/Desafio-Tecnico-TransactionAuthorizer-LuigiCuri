package com.luigi.desafiocaju.application.service;


import com.luigi.desafiocaju.domain.entity.Balance;
import com.luigi.desafiocaju.domain.entity.Category;
import com.luigi.desafiocaju.domain.exception.ErrorUpdateBalanceException;
import com.luigi.desafiocaju.domain.repository.BalanceRepository;
import com.luigi.desafiocaju.domain.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Service
public class BalanceService {

    @Autowired
    final BalanceRepository balanceRepository;
    @Autowired
    final MerchantRepository merchantRepository;

    public BalanceService(BalanceRepository balanceRepository, MerchantRepository merchantRepository) {
        this.balanceRepository = balanceRepository;
        this.merchantRepository = merchantRepository;
    }

    @Transactional
    public Boolean debitTransaction(Long accountId, Category category, BigDecimal amount) {
        try {
            return balanceRepository
                    .findByAccount_IdAndCategory(accountId, category)
                    .map(balance -> {
                        boolean hasSufficient = checkSufficientBalance(balance, amount);
                        if (hasSufficient) {
                            subtractAmount(balance, amount);
                        } else {
                            throw new ErrorUpdateBalanceException("ERROR: Insufficient funds to complete the transaction");
                        }
                        return hasSufficient;
                    })
                    .orElseGet(() -> {
                        return false;
                    });
        } catch (Exception e) {
            return false;
        }
    }


    private Boolean checkSufficientBalance(Balance balance, BigDecimal amount) {
        boolean hasSufficient = balance.getTotalAmount().compareTo(amount) >= 0;
        if (!hasSufficient) {
            throw new ErrorUpdateBalanceException("ERROR: Insufficient funds to complete the transaction");
        }
        return hasSufficient;
    }

    protected void subtractAmount(Balance balance, BigDecimal amount){
        BigDecimal updatedAmount = balance.getTotalAmount().subtract(amount);
        balance.setTotalAmount(updatedAmount);
        balanceRepository.save(balance);
    }

}
