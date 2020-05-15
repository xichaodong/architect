package com.chloe.model.vo.center;

import java.util.Date;
import java.util.List;

public class CenterOrderVO {
    private String orderId;

    private Integer payMethod;

    private Integer realPayAmount;

    private Integer postAmount;

    private Integer orderStatus;

    private Integer isComment;

    private Date createdTime;

    private List<CenterOrderItemVO> subOrderItemList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(Integer realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public Integer getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Integer postAmount) {
        this.postAmount = postAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<CenterOrderItemVO> getSubOrderItemList() {
        return subOrderItemList;
    }

    public void setSubOrderItemList(List<CenterOrderItemVO> subOrderItemList) {
        this.subOrderItemList = subOrderItemList;
    }
}
