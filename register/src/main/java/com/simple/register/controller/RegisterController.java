package com.simple.register.controller;

import com.simple.register.entiy.Register;
import com.simple.register.register.RegisterServer;
import com.simple.register.service.RegisterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: huangjun
 * @Date: 2022/7/14 17:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/mian")
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @GetMapping("/getRegister")
    public List<Register> getRegister(){
        return registerService.getRegister();
    }
}
