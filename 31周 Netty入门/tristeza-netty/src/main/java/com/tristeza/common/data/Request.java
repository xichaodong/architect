package com.tristeza.common.data;

import java.io.Serializable;

/**
 * @author chaodong.xi
 * @date 2021/1/25 下午8:32
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 5946296074542680265L;

    private String id;

    private String name;

    private String requestMessage;

    private byte[] attachment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
