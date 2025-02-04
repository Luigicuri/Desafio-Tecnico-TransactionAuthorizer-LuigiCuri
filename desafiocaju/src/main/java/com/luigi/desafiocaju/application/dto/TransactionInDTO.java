package com.luigi.desafiocaju.application.dto;

import java.math.BigDecimal;

public record TransactionInDTO(Long accountId, String mcc, BigDecimal amount, String merchant) {
}
