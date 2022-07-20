package com.simple.register;

import com.simple.register.register.RegisterServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class RegisterApplication implements CommandLineRunner {

    @Resource
    private RegisterServer registerServer;

    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        registerServer.run(9999);
    }
}
