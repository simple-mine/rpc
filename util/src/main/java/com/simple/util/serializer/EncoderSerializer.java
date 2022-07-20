package com.simple.util.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码
 * @Author: huangjun
 * @Date: 2022/7/12 17:42
 * @Version 1.0
 */
public class EncoderSerializer extends MessageToByteEncoder<Object> {
    /**
     * 我们知道在计算机中消息的交互都是byte 但是在netty中进行了封装所以在netty中基本的传递类型是ByteBuf
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        byte[] bytes = JavaSerializer.encodes(in);
        out.writeBytes(bytes);
    }
}