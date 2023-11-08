package com.sandeep.udemy.broker.watchlist;

import com.sandeep.udemy.broker.Symbol;
import com.sandeep.udemy.broker.data.InMemoryAccountStore;
import com.sandeep.udemy.watchlist.WatchList;
import com.sandeep.udemy.watchlist.WatchListController;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Stream;

@MicronautTest
public class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject
    @Client("/account/watchlist")
    HttpClient httpClient;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setup(){
        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    void returnsEmptyWatchListForTestAccount(){
        final WatchList result = httpClient.toBlocking().retrieve(HttpRequest.GET("/"), WatchList.class);
        Assertions.assertNull(result.symbols());
        Assertions.assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }

    @Test
    void returnsWatchListForTestAccount(){
        givenWatchLisForAccountExists();
        var response = httpClient.toBlocking().exchange("/", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(
    """
            {
                "symbols" : [ {
                    "value" : "AAPL"
                }, {
                    "value" : "GOOGL"
                }, {
                    "value" : "MSFT"
                } ]
            }""", response.getBody().get().toString());
    }

    private void givenWatchLisForAccountExists() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .toList()
        ));
    }

    @Test
    void canUpdateWatchListForTestAccount(){
        var symbols =  Stream.of("AAPL", "GOOGL", "MSFT")
                .map(Symbol::new)
                .toList();

        final var request = HttpRequest.PUT("/", new WatchList(symbols))
                                .accept(MediaType.APPLICATION_JSON);

        final HttpResponse<Object> added = httpClient.toBlocking().exchange(request);
        Assertions.assertEquals(HttpStatus.OK, added.getStatus());
        Assertions.assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());
    }

    @Test
    void canDeleteWatchListForTestAccount(){
        givenWatchLisForAccountExists();
        Assertions.assertFalse(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());

        final var deleted = httpClient.toBlocking().exchange(HttpRequest.DELETE("/"));

        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleted.getStatus());
        Assertions.assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }
}
