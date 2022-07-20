package com.simple.util.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/** 解码
 * @Author: huangjun
 * @Date: 2022/7/12 17:41
 * @Version 1.0
 */
public class DecoderSerializer extends ByteToMessageDecoder {
    private static final int LENGTH = 4;

    /**
     * 解码时传进来的参数是ByteBuf也就是Netty的基本单位，而传出的是解码后的Object
     */
    @Override
    protected void decode(ChannelHandlerContext arg0, ByteBuf in, List<Object> out) throws Exception {
        int size = in.readableBytes();
        if (size > DecoderSerializer.LENGTH) {
            byte[] bytes = JavaSerializer.getByteFromBuf(in);
            Object info = JavaSerializer.decode(bytes);
            out.add(info);
        }

    }
}
