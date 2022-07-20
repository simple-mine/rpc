package com.simple.rpc.register;

import com.simple.util.entiy.TCPConfig;
import com.simple.util.serializer.DecoderSerializer;
import com.simple.util.serializer.EncoderSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 15:34
 * @Version 1.0
 */
public class ClientServer {

    private final static Integer NETTYPORT = 9999;

    public static void run(String ip,Map<String, TCPConfig> tcpConfigMap) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            //创建bootstrap对象配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventLoopGroup)
                    //设置客户端通道实现类型
                    .channel(NioSocketChannel.class)
                    //初始化通道
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new EncoderSerializer());
                            ch.pipeline().addLast(new DecoderSerializer());
                            ch.pipeline().addLast(new ClientServerHandler());
                        }
                    });
            System.out.println("客户端准备完毕");
            //连接服务器
            Channel channel = bootstrap.connect(ip, NETTYPORT).sync().channel();
            channel.writeAndFlush(tcpConfigMap);
            channel.flush();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭线程组
            eventLoopGroup.shutdownGracefully();
        }
    }
}
