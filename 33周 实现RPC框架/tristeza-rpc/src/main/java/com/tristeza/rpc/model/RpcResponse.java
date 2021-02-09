package com.tristeza.rpc.model;

import java.io.Serializable;

/**
 * @author chaodong.xi
 * @date 2021/2/9 5:49 下午
 */
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = -1587872429162375605L;

    private String requestId;

    private Object result;

    private Throwable throwable;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
