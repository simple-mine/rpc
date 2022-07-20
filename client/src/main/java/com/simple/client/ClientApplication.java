package com.simple.client;


import com.simple.rpc.annotations.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRPC(referencePackage = "com.simple.client.controller",clientRegisterIp = "127.0.0.1")
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
