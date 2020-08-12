package com.tristeza.springframework.enums;

/**
 * @author chaodong.xi
 * @date 2020/8/11 11:44 下午
 */
public enum ProtocolType {
    FILE("file", "文件");
    private final String code;
    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    ProtocolType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
