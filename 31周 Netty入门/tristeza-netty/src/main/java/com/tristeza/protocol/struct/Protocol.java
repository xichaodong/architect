package com.tristeza.protocol.struct;

/**
 * @author chaodong.xi
 * @date 2021/1/26 下午8:16
 */
public class Protocol {
    private Header header;

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
