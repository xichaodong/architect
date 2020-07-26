package com.tristeza.shardingjdbc.model;

import java.math.BigDecimal;

/**
 * @author chaodong.xi
 * @date 2020/7/15 8:27 下午
 */
public class ShardingOrder {
    private Integer id;

    private BigDecimal orderAmount;

    private Integer orderStatus;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
