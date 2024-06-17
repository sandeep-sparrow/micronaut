package com.sandeep.udemy.broker.watchlist;

import com.sandeep.udemy.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import static com.sandeep.udemy.broker.data.InMemoryAccountStore.ACCOUNT_ID;

@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get(){
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList){
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Void> delete(){
        store.deleteWatchList(ACCOUNT_ID);
        return HttpResponse.noContent();
    }
}
