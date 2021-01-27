package com.tristeza.enums;

/**
 * @author chaodong.xi
 * @date 2021/1/26 下午7:27
 */
public enum MessageType {
    SERVICE_REQ((byte) 0), //业务请求消息
    SERVICE_RESP((byte) 1), //业务响应消息
    ONE_WAY((byte) 2), //即是请求消息也是响应消息
    LOGIN_REQ((byte) 3), //握手请求消息
    LOGIN_RESP((byte) 4), //握手响应消息
    HEARTBEAT_REQ((byte) 5), //心跳请求消息
    HEARTBEAT_RESP((byte) 6); //心跳响应消息

    private final byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
