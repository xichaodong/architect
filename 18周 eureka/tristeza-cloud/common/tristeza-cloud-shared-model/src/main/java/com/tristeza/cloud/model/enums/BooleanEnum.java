package com.tristeza.cloud.model.enums;

/**
 * 性别枚举
 */
public enum BooleanEnum {
    FALSE(0, "否"),
    TRUE(1, "是");

    public final Integer type;
    public final String value;

    BooleanEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
