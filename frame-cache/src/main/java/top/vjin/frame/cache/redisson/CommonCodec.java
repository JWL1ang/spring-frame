/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:57:13
 */

package top.vjin.frame.cache.redisson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nustaq.serialization.FSTConfiguration;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.codec.FstCodec;

import java.io.IOException;
import java.io.Serializable;

/**
 * 通用redisson序列化器，如果目标实现了Serializable接口则使用jdk序列化，否则打包json后序列化
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class CommonCodec extends FstCodec implements Decoder<Object>, Encoder {

    /** json序列化类 */
    private ObjectMapper objectMapper = new ObjectMapper();

    public CommonCodec() {
    }

    public CommonCodec(ClassLoader classLoader) {
        super(classLoader);
    }

    public CommonCodec(ClassLoader classLoader, CommonCodec codec) {
        super(classLoader, codec);
    }

    public CommonCodec(FSTConfiguration fstConfiguration) {
        super(fstConfiguration);
    }

    @Override
    public ByteBuf encode(Object in) throws IOException {
        if (in == null) return super.getValueEncoder().encode(null);
        if (in instanceof Serializable) {
            return super.getValueEncoder().encode(in);
        }

        ObjectPack pack = new ObjectPack(in.getClass(), objectMapper.writeValueAsString(in));
        return super.getValueEncoder().encode(pack);
    }

    @Override
    public Object decode(ByteBuf buf, State state) throws IOException {
        Object decode = super.getValueDecoder().decode(buf, state);
        if (decode instanceof ObjectPack) {
            ObjectPack pack = (ObjectPack) decode;
            return objectMapper.readValue(pack.getText(), pack.getClazz());
        }
        return decode;
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return this;
    }

    @Override
    public Encoder getValueEncoder() {
        return this;
    }

    /**
     * 对象包装器,对于未实现Serializable接口的类,先转json,再用本类存放json字符串后调用jdk序列化.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ObjectPack implements Serializable {

        private static final long serialVersionUID = 3544258756813023826L;

        /** 类型 */
        private Class<?> clazz;

        /** json数据 */
        private String text;
    }
}
