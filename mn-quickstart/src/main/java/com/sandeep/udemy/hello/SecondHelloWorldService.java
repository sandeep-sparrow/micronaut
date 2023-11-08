package com.sandeep.udemy.hello;

import jakarta.inject.Singleton;

@Singleton
public class SecondHelloWorldService implements MyService {

    @Override
    public String helloFromService(){
        return "Hello From Second Service!";
    }
}
