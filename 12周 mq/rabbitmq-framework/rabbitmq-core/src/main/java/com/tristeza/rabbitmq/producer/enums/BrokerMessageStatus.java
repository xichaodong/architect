package com.tristeza.rabbitmq.producer.enums;

/**
 * @author chaodong.xi
 * @date 2020/7/7 3:51 下午
 */
public enum BrokerMessageStatus {
    SENDING("0", "发送中"),
    SEND_OK("1", "发送成功"),
    SEND_FAIL("2", "发送失败");

    private String code;
    private String desc;

    BrokerMessageStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
