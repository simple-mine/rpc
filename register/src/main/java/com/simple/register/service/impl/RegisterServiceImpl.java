package com.simple.register.service.impl;

import com.simple.register.entiy.Register;
import com.simple.register.register.RegisterServerHandler;
import com.simple.register.service.RegisterService;
import com.simple.util.entiy.TCPConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: huangjun
 * @Date: 2022/7/14 17:15
 * @Version 1.0
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Override
    public List<Register> getRegister() {
        Map<String, Map<String, TCPConfig>> allServerMap = RegisterServerHandler.getAllServerMap();
        if (allServerMap.size() > 0){
            List<Register> list = new ArrayList<>();
            for (String key : allServerMap.keySet()) {
                String ip = key.substring(1,key.indexOf(":"));
                Map<String, TCPConfig> tcpConfigMap = allServerMap.get(key);
                for (String s : tcpConfigMap.keySet()) {
                    Register register = new Register();
                    register.setIp(ip);
                    register.setServerName(s);
                    Boolean server = tcpConfigMap.get(s).getServer();
                    register.setRoleName(server == null ||server ? "提供者" : "消费者");
                    list.add(register);
                }
            }
            return list;
        }else {
            return new ArrayList<>();
        }
    }
}
