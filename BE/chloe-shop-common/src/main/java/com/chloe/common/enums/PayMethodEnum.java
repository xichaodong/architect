package com.chloe.common.enums;

/**
 * 支付方式枚举
 */
public enum PayMethodEnum {
    WX(1, "微信"),
    ALI_PAY(2, "支付宝");

    public final Integer type;
    public final String value;

    PayMethodEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
