package com.simple.register.register;

import com.simple.util.serializer.DecoderSerializer;
import com.simple.util.serializer.EncoderSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: huangjun
 * @Date: 2022/7/11 15:34
 * @Version 1.0
 */
@Slf4j
@Component
public class RegisterServer {

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        log.info("准备运行端口：{}" , port);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    //设置服务端通信实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG,128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new DecoderSerializer());
                            ch.pipeline().addLast(new EncoderSerializer());
                            ch.pipeline().addLast(new RegisterServerHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("注册中心已启动");
            //等待服务监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            //退出，释放线程资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
