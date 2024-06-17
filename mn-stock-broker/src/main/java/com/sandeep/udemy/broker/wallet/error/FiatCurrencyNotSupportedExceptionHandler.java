package com.sandeep.udemy.broker.wallet.error;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import static com.sandeep.udemy.broker.wallet.WalletController.SUPPORTED_FLAT_CURRENCIES;

@Produces
@Singleton
@Requires(classes = {FiatCurrencyNotSupportedExceptionHandler.class, FiatCurrencyNoSupportedException.class})
public class FiatCurrencyNotSupportedExceptionHandler implements ExceptionHandler<FiatCurrencyNoSupportedException, HttpResponse<CustomError>> {

    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, FiatCurrencyNoSupportedException exception) {
        return HttpResponse.badRequest(
            new CustomError(
                    HttpStatus.BAD_REQUEST.getCode(),
                    "UNSUPPORTED_FLAT_CURRENCY",
                    String.format("Only %s are supported", SUPPORTED_FLAT_CURRENCIES)
            )
        );
    }
}
