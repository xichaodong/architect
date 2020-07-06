package com.tristeza.rabbit.common.serialize.serializer;

/**
 * 序列化和反序列化
 *
 * @author chaodong.xi
 * @date 2020/7/6 12:50 下午
 */
public interface Serializer {
    byte[] serializer2Byte(Object data);

    String serializer2String(Object data);

    <T> T deserializer(byte[] content);

    <T> T deserializer(String content);
}
