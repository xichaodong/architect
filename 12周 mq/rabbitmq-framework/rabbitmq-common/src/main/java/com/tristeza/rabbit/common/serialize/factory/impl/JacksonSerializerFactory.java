package com.tristeza.rabbit.common.serialize.factory.impl;

import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbit.common.serialize.serializer.impl.JacksonSerializer;
import com.tristeza.rabbit.common.serialize.serializer.Serializer;
import com.tristeza.rabbit.common.serialize.factory.SerializerFactory;

/**
 * @author chaodong.xi
 * @date 2020/7/6 7:49 下午
 */
public class JacksonSerializerFactory implements SerializerFactory {
    public static final JacksonSerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
