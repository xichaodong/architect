package com.tristeza.rabbit.api.exception;

/**
 * @author chaodong.xi
 * @date 2020/7/5 1:23 下午
 */
public class MessageRunTimeException extends RuntimeException {

    private static final long serialVersionUID = 6843180192768456497L;

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message) {
        super(message);
    }

    public MessageRunTimeException(Throwable cause) {
        super(cause);
    }

    public MessageRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
