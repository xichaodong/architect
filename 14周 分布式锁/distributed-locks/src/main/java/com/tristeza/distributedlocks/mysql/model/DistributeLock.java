package com.tristeza.distributedlocks.mysql.model;

/**
 * @author chaodong.xi
 * @date 2020/7/15 8:27 下午
 */
public class DistributeLock {
    private Integer id;

    private String businessCode;

    private String businessName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        return "DistributeLock{" +
                "id=" + id +
                ", businessCode='" + businessCode + '\'' +
                ", businessName='" + businessName + '\'' +
                '}';
    }
}
