package com.simple.rpc.socket;

import com.simple.rpc.main.RPCMain;
import com.simple.util.entiy.TCPConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author: huangjun
 * @Date: 2022/7/12 18:01
 * @Version 1.0
 */
public class SocketRequest {

    public static Object sendRequest(Method method, Object[] args){
        // 获取到远程类
        String methodName = method.getName();
        String serverName = method.getDeclaringClass().getTypeName() + "."+methodName;
        TCPConfig tcpConfig = RPCMain.getTcpMap().get(serverName);
        if (tcpConfig == null){
            throw new RuntimeException("can not find provider");
        }
        return sendAndReceive(tcpConfig.getIp(),tcpConfig.getPort(),serverName,methodName,args);
    }

    private static Object sendAndReceive(String ip, int port,String serverName,String methodName, Object[] args){
        //开启一个链接，需要指定地址和端口
        try (Socket client = new Socket(ip, port)){

            //向输出流中写入数据，传向服务端
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(serverName);
            objectOutputStream.writeObject(methodName);
            objectOutputStream.writeObject(args);

            //从输入流中解析数据，输入流来自服务端的响应
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            return objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("invoke error");
        }
    }
}
