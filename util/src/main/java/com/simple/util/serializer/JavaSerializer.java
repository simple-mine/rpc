package com.simple.util.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;

/**Java序列化工具
 * @Author: huangjun
 * @Date: 2022/7/12 17:23
 * @Version 1.0
 */
public class JavaSerializer {

    static byte[] encodes(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        byte[] bytes = out.toByteArray();
        return bytes;
    }

    static Object decode(byte[] bytes) throws IOException, ClassNotFoundException {
        // 对象返序列化
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream inn = new ObjectInputStream(in);
        Object obj = inn.readObject();
        return obj;
    }

    /**
     * byte to buf
     *
     * @param bytes
     * @return
     */
    public ByteBuf getBufFromByte(byte[] bytes) {
        ByteBuf buf = Unpooled.copiedBuffer(bytes);
        return buf;
    }

    /**
     * buf to byte
     *
     * @param buf
     * @return
     */
    static byte[] getByteFromBuf(ByteBuf buf) {
        int size = buf.readableBytes();
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);
        return bytes;
    }
}
