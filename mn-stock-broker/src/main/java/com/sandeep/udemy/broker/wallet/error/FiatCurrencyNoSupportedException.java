package com.sandeep.udemy.broker.wallet.error;

public class FiatCurrencyNoSupportedException extends RuntimeException {

    public FiatCurrencyNoSupportedException(String message) {
        super(message);
    }
}
