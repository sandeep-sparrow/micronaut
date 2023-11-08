package com.sandeep.udemy.broker.wallet;

import com.sandeep.udemy.broker.Symbol;
import com.sandeep.udemy.broker.wallet.api.RestApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet (
    UUID accountId,
    UUID walletId,
    Symbol symbol,
    BigDecimal available,
    BigDecimal locked
) implements RestApiResponse {

    public Wallet addAvailable(BigDecimal amountToAdd){
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available().add(amountToAdd),
                this.locked
        );
    }
}
