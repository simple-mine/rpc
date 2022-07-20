package com.simple.rpc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: huangjun
 * @Date: 2022/7/13 11:27
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableRPC {

    //消费者需要注入远程接口的包名
    String referencePackage() default "";

    //注册中心ip
    String clientRegisterIp() default "";
}
