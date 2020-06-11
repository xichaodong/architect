package com.chloe.model.vo;

import com.chloe.model.bo.CartBO;

import java.util.List;

/**
 * 订单创建VO
 */
public class CreateOrderVO {
    private String orderId;

    private MerchantOrdersVO merchantOrdersVO;

    private List<CartBO> needRemoveBOS;

    public List<CartBO> getNeedRemoveBOS() {
        return needRemoveBOS;
    }

    public void setNeedRemoveBOS(List<CartBO> needRemoveBOS) {
        this.needRemoveBOS = needRemoveBOS;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}
