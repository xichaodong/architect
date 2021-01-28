package com.tristeza.model;

import com.google.protobuf.GeneratedMessageV3;
import com.tristeza.protobuf.MessageModule;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午2:22
 */
public class Response<T extends GeneratedMessageV3> {
    private MessageModule.ResultType resultType;
    private T content;

    public static <T extends GeneratedMessageV3> Response<T> buildResponse(MessageModule.ResultType resultType, T content) {
        Response<T> response = new Response<>();
        response.setResultType(resultType);
        response.setContent(content);
        return response;
    }

    public static <T extends GeneratedMessageV3> Response<T> success(T content) {
        return buildResponse(MessageModule.ResultType.SUCCESS, content);
    }

    public static <T extends GeneratedMessageV3> Response<T> success() {
        return buildResponse(MessageModule.ResultType.SUCCESS, null);
    }

    public static <T extends GeneratedMessageV3> Response<T> failure(T content) {
        return buildResponse(MessageModule.ResultType.FAILURE, content);
    }

    public static <T extends GeneratedMessageV3> Response<T> failure() {
        return buildResponse(MessageModule.ResultType.FAILURE, null);
    }

    public MessageModule.ResultType getResultType() {
        return resultType;
    }

    public void setResultType(MessageModule.ResultType resultType) {
        this.resultType = resultType;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
