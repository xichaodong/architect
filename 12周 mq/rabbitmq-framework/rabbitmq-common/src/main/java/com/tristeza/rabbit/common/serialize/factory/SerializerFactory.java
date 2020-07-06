package com.tristeza.rabbit.common.serialize.factory;

import com.tristeza.rabbit.common.serialize.serializer.Serializer;

/**
 * @author chaodong.xi
 * @date 2020/7/6 12:49 下午
 */
public interface SerializerFactory {
    Serializer create();
}
