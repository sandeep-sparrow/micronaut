package com.sandeep.udemy.broker.data;

import com.sandeep.udemy.broker.Symbol;
import com.sandeep.udemy.broker.wallet.DepositFaitMoney;
import com.sandeep.udemy.broker.wallet.Wallet;
import com.sandeep.udemy.broker.wallet.WithdrawFaitMoney;
import com.sandeep.udemy.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class InMemoryAccountStore {

    public static final UUID ACCOUNT_ID = UUID.fromString("f4245629-83df-4ed8-90d9-7401045b5921");

    private final Map<UUID, WatchList> watchListPerAccount = new HashMap<>();
    private final Map<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();

    public WatchList getWatchList(UUID accountId) {
        return watchListPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList){
        watchListPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchList(final UUID accountId){
        watchListPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletsPerAccount.get(accountId))
                .orElse(new HashMap<>())
                .values();
    }

    public Wallet depositToWallet(DepositFaitMoney deposit) {
        return addAvailableInWallet(deposit.accountId(), deposit.walletId(), deposit.symbol(), deposit.amount());
    }

    public Wallet withdrawFromWallet(WithdrawFaitMoney withdraw) {
        return addAvailableInWallet(withdraw.accountId(), withdraw.walletId(), withdraw.symbol(), withdraw.amount());
    }

    private Wallet addAvailableInWallet(UUID accountId, UUID walletId, Symbol symbol, BigDecimal amount) {
        final var wallets = Optional.ofNullable(
                walletsPerAccount.get(accountId)
        ).orElse(
                new HashMap<>()
        );

        var oldWallet = Optional.ofNullable(wallets.get(walletId)
        ).orElse(
                new Wallet(ACCOUNT_ID, walletId, symbol, BigDecimal.ZERO, BigDecimal.ZERO)
        );

        var newWallet = oldWallet.addAvailable(amount);
        // Update the wallet
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }
}
