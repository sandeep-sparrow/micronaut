package com.sandeep.udemy.broker.wallet;

import com.sandeep.udemy.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositFaitMoney (
    UUID accountId,
    UUID walletId,
    Symbol symbol,
    BigDecimal amount
) {
}
