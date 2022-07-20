package com.simple.server;

import com.simple.rpc.annotations.StratRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StratRPC(registerPackage = "com.simple.server.service.impl",serverPort = 7777,serverRegisterIp = "127.0.0.1")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
