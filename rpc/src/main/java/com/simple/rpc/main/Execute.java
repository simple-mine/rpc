package com.simple.rpc.main;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.simple.rpc.factory.ServerInterfaceFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huangjun
 * @Date: 2022/7/13 10:10
 * @Version 1.0
 */
public class Execute {

    private static ThreadFactory namedFactory = new ThreadFactoryBuilder().setNameFormat("provid-thread-%d").build();

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new
            LinkedTransferQueue<>(), namedFactory, new ThreadPoolExecutor.AbortPolicy());

    public static void startTpcServer(String ip, int serverPort){
        threadPool.execute(()->{
            executeRequest(ip,serverPort);
        });
    }

    private static void executeRequest(String ip, int serverPort){
        try {
            //封装服务端地址
            InetAddress serverAddress = InetAddress.getByName(ip);
            //建立服务端
            try (ServerSocket service = new ServerSocket(serverPort, 10, serverAddress)) {
                while (true) {
                    //接受一个连接，该方法会阻塞程序，直到一个链接到来
                    try (Socket connect = service.accept()) {

                        //解析输入流，该输入流来自客户端
                        ObjectInputStream objectInputStream = new ObjectInputStream(connect.getInputStream());

                        String serverName = (String) objectInputStream.readObject();
                        String methodName = (String) objectInputStream.readObject();
                        Object[] args = (Object[]) objectInputStream.readObject();

                        if (serverName != null && methodName != null && args != null){
                            Map<String, Class<?>> serviceMap = RPCMain.getServiceMap();
                            Class<?> aClass1 = serviceMap.get(serverName);

                            Object invoke = "";
                            //查找对应的方法
                            Method[] methods = aClass1.getMethods();
                            for (Method method : methods) {
                                //调用方法
                                if (methodName.equals(method.getName())){
                                    //创建代理对象
                                    invoke  = new ServerInterfaceFactory(aClass1.newInstance()).invoke(aClass1, method, args);
                                }
                            }
                            //组建响应信息
                            OutputStream out = connect.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                            objectOutputStream.writeObject(invoke);
                        }else {
                            //获取输入流，并通过向输出流写数据的方式发送响应
                            OutputStream out = connect.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                            objectOutputStream.writeObject("can not find provid");
                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
