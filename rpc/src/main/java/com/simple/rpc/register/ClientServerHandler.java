package com.simple.rpc.register;

import com.simple.rpc.main.RPCMain;
import com.simple.util.entiy.TCPConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 15:35
 * @Version 1.0
 */
@ChannelHandler.Sharable
public class ClientServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map map = (Map) msg;
        Map<String, TCPConfig> tcpMap = RPCMain.getTcpMap();
        //向本地的tcpMap中添加注册中心返回的其他服务信息
        for (Object key : map.keySet()) {
            tcpMap.put(key.toString(), (TCPConfig) map.get(key));
        }
        RPCMain.setTcpMap(tcpMap);
    }
}

