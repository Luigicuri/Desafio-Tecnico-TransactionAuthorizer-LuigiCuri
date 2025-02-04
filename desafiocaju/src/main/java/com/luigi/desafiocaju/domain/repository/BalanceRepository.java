package com.luigi.desafiocaju.domain.repository;

import com.luigi.desafiocaju.domain.entity.Balance;
import com.luigi.desafiocaju.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance,Long> {

    Optional<Balance> findByAccount_IdAndCategory(Long accountId, Category category);

    @Modifying
    @Query("UPDATE Balance b SET b.totalAmount = :amount WHERE b.id = :id ")
    int updateBalance(Long id, BigDecimal amount);
}
