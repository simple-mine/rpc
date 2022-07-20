package com.simple.client.controller;

import com.simple.api.service.HelloService;
import com.simple.rpc.annotations.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 18:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/hello")
public class HelloController {


    @Reference(name = "helloService")
    private HelloService helloService;

    @GetMapping("/say")
    public String sayHello(String world){
        return helloService.sayHello(world);
    }
}
