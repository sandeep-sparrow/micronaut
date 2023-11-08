package com.sandeep.udemy.broker.wallet;

import com.sandeep.udemy.broker.data.InMemoryAccountStore;
import com.sandeep.udemy.broker.wallet.api.RestApiResponse;
import com.sandeep.udemy.broker.wallet.error.CustomError;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static com.sandeep.udemy.broker.data.InMemoryAccountStore.ACCOUNT_ID;

@Controller("/account/wallets")
public record WalletController(InMemoryAccountStore store) {

    private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);
    public static final List<String> SUPPORTED_FLAT_CURRENCIES = List.of("EUR","INR","CHF","GBP");

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet> get(){
        return store.getWallets(ACCOUNT_ID);
    }

    @Post(
            value = "/deposit",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<RestApiResponse> depositFiatMoney(@Body DepositFaitMoney deposit){
        // Option 1: Custom HttpResponse
        if(!SUPPORTED_FLAT_CURRENCIES.contains(deposit.symbol().value())){
            return HttpResponse.badRequest()
                    .body(new CustomError(
                            HttpStatus.BAD_REQUEST.getCode(),
                            "UNSUPPORTED_FLAT_CURRENCY",
                            String.format("Only %s are supported", SUPPORTED_FLAT_CURRENCIES)
                    ));
        }
        Wallet wallet = store.depositToWallet(deposit);
        LOG.debug("Deposit to wallet: {}", wallet);
        return HttpResponse.ok().body(wallet);
    }

    @Post(
            value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Void withdrawFiatMoney(@Body WithdrawFaitMoney deposit){
        // Option 2: Customer Error Processing
        return null;
    }

}
