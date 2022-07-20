package com.simple.server.service.impl;

import com.simple.api.service.HelloService;
import com.simple.rpc.annotations.RegisterService;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 18:22
 * @Version 1.0
 */
@RegisterService(name = "helloService")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String world) {
        return "sayHello ==>" + world;
    }
}
