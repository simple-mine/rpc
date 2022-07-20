package com.simple.register.register;

import com.simple.util.entiy.TCPConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 15:35
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
public class RegisterServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(RegisterServerHandler.class.getName());

    //接入的所有Channel
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //保存所有的服务端信息，key值为ip地址
    private static Map<String, Map<String, TCPConfig>> allServerMap = new HashMap<>();

    public static Map<String, Map<String, TCPConfig>> getAllServerMap() {
        return allServerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传入消息的Channel
        Channel inComing = ctx.channel();
        try {
            if (msg != null) {
                Map map = (Map) msg;
                for (Channel channel : channels) {
                    if (channel != inComing) {
                        if ( map.size() > 0){
                            //为现有已注册的服务广播该服务
                            channel.writeAndFlush(map);
                        }
                    } else {
                        //为当前注册进来服务返回现有的服务信息
                        Map<String, TCPConfig> temp = new HashMap<>();
                        for (Map<String, TCPConfig> value : allServerMap.values()) {
                            temp.putAll(value);
                        }
                        channel.writeAndFlush(temp);
                    }
                }
                ctx.flush();
                //保存服务端信息
                allServerMap.put(inComing.remoteAddress().toString(), map);
            }
        } catch (Exception e) {
            inComing.closeFuture().sync();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString()+"退出注册");
        channels.remove(ctx.channel());
        allServerMap.remove(ctx.channel().remoteAddress().toString());
        //广播通知更新后的服务信息
        for (Channel channel : channels) {
            channel.writeAndFlush(allServerMap.values());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + "注册");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常处理
        System.out.println("与客户端[" + ctx.channel().remoteAddress() + "]通信发生异常：");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 读取数据超时   --->  断定连接断开  ----> 释放对应的socket连接
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        try {
            Channel channel = ctx.channel();
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
                    channel.closeFuture().sync();  //call back channelInactive(ChannelHandlerContext ctx)
                    logger.info(channel.remoteAddress() + "---No data was received for a while ,read time out... ...");
                }
                else if (e.state() == IdleState.WRITER_IDLE) { // No data was sent for a while.
                    channel.closeFuture().sync();
                    logger.info(channel.remoteAddress() + "---No data was sent for a while.write time out... ...");
                }
            } else if (evt instanceof ChannelInputShutdownReadComplete) {
                channel.closeFuture().sync();//远程主机强制关闭连接
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

