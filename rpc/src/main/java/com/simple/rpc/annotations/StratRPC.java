package com.simple.rpc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: huangjun
 * @Date: 2022/7/12 10:01
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StratRPC {

    //需要注册的服务所在的包
    String registerPackage() default "";

    //服务监听的端口
    int serverPort() default 0;

    //注册中心ip
    String serverRegisterIp() default "";


}
