package com.tristeza.rabbit.api.enums;

/**
 * @author chaodong.xi
 * @date 2020/7/5 12:55 下午
 */
public interface MessageType {
    /**
     * 迅速消息：不需要可靠性保证，也不需要做消息送达确认
     */
    Integer RAPID = 0;

    /**
     * 确认消息：不需要可靠性保证，但是需要做消息送达确认
     */
    Integer CONFIRM = 1;

    /**
     * 可靠消息：需要100%可靠性保证和消息送达确认
     */
    Integer RELIANT = 2;
}
