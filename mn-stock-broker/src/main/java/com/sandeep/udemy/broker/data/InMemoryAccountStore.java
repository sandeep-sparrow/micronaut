package com.sandeep.udemy.broker.data;

import com.sandeep.udemy.watchlist.WatchList;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class InMemoryAccountStore {

    private final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<>();

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
}
