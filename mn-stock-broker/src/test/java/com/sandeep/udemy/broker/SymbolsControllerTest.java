package com.sandeep.udemy.broker;

import com.fasterxml.jackson.databind.JsonNode;
import com.sandeep.udemy.broker.data.InMemoryStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MicronautTest
class SymbolsControllerTest {

    private final static Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/symbols")
    HttpClient httpClient;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    void setup(){
        inMemoryStore.initializeWith(10);
    }

    @Test
    void symbolsEndpointReturnsListOfSymbols(){
        var response = httpClient.toBlocking().exchange("/", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbolsEndpointReturnsCorrectSymbol(){
        var testSymbol = new Symbol("MEOH");
        inMemoryStore.getSymbols().put(testSymbol.value(), testSymbol);
        var response = httpClient.toBlocking().exchange("/" + testSymbol.value(), Symbol.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(testSymbol, response.getBody().get());
    }

    @Test
    void symbolsEndpointReturnsCorrectTakingUsingQueryParameterIntoAccount(){
        var max10  = httpClient.toBlocking().exchange("/filter?max=10", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, max10.getStatus());
        LOG.debug("Max: 10: {}", max10.getBody().get().toPrettyString());
        Assertions.assertEquals(10, max10.getBody().get().size());

        var offset7  = httpClient.toBlocking().exchange("/filter?offset=7", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, offset7.getStatus());
        LOG.debug("Offset: 7: {}", offset7.getBody().get().toPrettyString());
        Assertions.assertEquals(3, offset7.getBody().get().size());

        var max2Offset7  = httpClient.toBlocking().exchange("/filter?max=2&offset=7", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, max2Offset7.getStatus());
        LOG.debug("Max : 2, Offset: 7: {}", max2Offset7.getBody().get().toPrettyString());
        Assertions.assertEquals(2, max2Offset7.getBody().get().size());
    }

}
