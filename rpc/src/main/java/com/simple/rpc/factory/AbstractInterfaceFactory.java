package com.simple.rpc.factory;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 12:00
 * @Version 1.0
 */

import com.simple.rpc.socket.SocketRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

/**
 * 代理工厂
 */
public abstract class AbstractInterfaceFactory {
    protected static final Logger logger = Logger.getLogger(AbstractInterfaceFactory.class.getName());

    public static AbstractInterfaceFactory getInstance() {
        return new DefaultInterfaceFactory();
    }

    protected AbstractInterfaceFactory(){}

    public <T> T getWebService(Class<T> oldInterface) {
        InterfaceHandler intr = new InterfaceHandler(this);
        return  (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{oldInterface}, new InterfaceHandler(this));
    }

    /**
     * 子类实现
     */
    protected abstract Object remoteCall(Method methodName, Object[] args);

    /**
     * 代理类
     */
    private static final class InterfaceHandler implements InvocationHandler {
        private AbstractInterfaceFactory factory;

        public InterfaceHandler(AbstractInterfaceFactory factory) {
            this.factory = factory;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            String remoteMethodName = method.getName();
            logger.info("开始调用接口：" + remoteMethodName);
            Object rst = factory.remoteCall(method, args);
            logger.info("完成调用");
            return rst;
        }
    }

    /**
     * 静态工厂的默认实现
     */
    private static final class DefaultInterfaceFactory extends AbstractInterfaceFactory {
        @Override
        protected Object remoteCall(Method method, Object[] args) {
            logger.info("远程方法调用中.....");
            return SocketRequest.sendRequest(method,args);
        }
    }
}
