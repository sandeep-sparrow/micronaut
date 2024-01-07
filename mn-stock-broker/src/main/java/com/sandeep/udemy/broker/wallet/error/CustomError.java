package com.sandeep.udemy.broker.wallet.error;

import com.sandeep.udemy.broker.wallet.api.RestApiResponse;

public record CustomError(
        int status,
        String error,
        String message
) implements RestApiResponse {
}
