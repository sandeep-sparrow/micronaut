package com.sandeep.udemy;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@MicronautTest
class HelloWorldControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void helloWorldEndPointResponseWithProperContent(){
        var response = httpClient.toBlocking().retrieve("/hello");
        Assertions.assertEquals("Hello From Service!", response);
    }

    @Test
    void helloWorldEndPointResponseWithProperStatusCodeAndContent(){
        var response = httpClient.toBlocking().exchange("/hello", String.class);
        Assertions.assertEquals("Hello From Service!", response.getBody().get());
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void helloFromConfigEndpointReturnMessageFromConfigFile(){
        var response = httpClient.toBlocking().exchange("/hello/config", String.class);
        Assertions.assertEquals("Hello from application.yml", response.getBody().get());
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void helloFromTranslationEndpointReturnContentFromConfigFile(){
        var response = httpClient.toBlocking().exchange("/hello/translation", JsonNode.class);
        Assertions.assertEquals("{\"de\":\"Hallo Welt\",\"en\":\"Hello World\"}", response.getBody().get().toString());
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

}
