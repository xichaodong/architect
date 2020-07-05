package com.tristeza.rabbit.api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author chaodong.xi
 * @date 2020/7/5 1:23 下午
 */
public class MessageException extends Exception {

    private static final long serialVersionUID = 8062445622571172192L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
