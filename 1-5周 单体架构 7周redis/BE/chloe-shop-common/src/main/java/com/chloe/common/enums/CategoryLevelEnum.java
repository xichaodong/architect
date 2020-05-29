package com.chloe.common.enums;

/**
 * 性别枚举
 */
public enum CategoryLevelEnum {
    ROOT(1, "一级分类"),
    SUB(2, "二级分类"),
    THIRD(3, "三级分类");

    public final Integer type;
    public final String value;

    CategoryLevelEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
