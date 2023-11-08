package com.sandeep.udemy.hello;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/hello")
public class HelloWorldController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldController.class);

    private final MyService myService;
    private final String helloFromConfig;
    private final HelloWorldTranslationConfig translationConfig;

    public HelloWorldController(MyService myService,
                                @Property(name = "hello.world.message") String helloFromConfig,
                                HelloWorldTranslationConfig translationConfig){
        this.myService = myService;
        this.helloFromConfig = helloFromConfig;
        this.translationConfig = translationConfig;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld(){
        LOG.debug("Called the Hello World API");
        return myService.helloFromService();
    }

    @Get(uri = "/config", produces = MediaType.TEXT_PLAIN)
    public String helloConfig(){
        LOG.debug("Return Hello from config message: {}", helloFromConfig);
        return helloFromConfig;
    }

    @Get(uri = "/translation", produces = MediaType.APPLICATION_JSON)
    public HelloWorldTranslationConfig helloTranslation(){
        return translationConfig;
    }
}
