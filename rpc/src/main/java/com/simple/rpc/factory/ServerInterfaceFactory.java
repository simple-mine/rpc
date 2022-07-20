package com.simple.rpc.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: huangjun
 * @Date: 2022/7/8 16:53
 * @Version 1.0
 */
public class ServerInterfaceFactory implements InvocationHandler {
    /**
     * 1. 目标类
     */
    private final Object target;

    public ServerInterfaceFactory(Object target) {
        this.target = target;
    }

    /**
     * 2. 代理逻辑
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //调用目标方法
        Object result = null;
        try {
            //前置通知
            result = method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
            //异常通知, 可以访问到方法出现的异常
            System.out.println( "方法调用出现了异常");
        }
        //后置通知.
        System.out.println( "方法调用完成！");
        return result;
    }

    /**
     * 3. 获取目标类代理
     */
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),target.getClass().getInterfaces(),this);
    }
}
