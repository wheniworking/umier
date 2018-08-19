package com.longwei.umier.entity;

import java.util.Date;

public class CourseGroupActivityOrder {

    public enum PayState{
        NOT_PAY(0),PAY_SUCCESS(1),PAY_FAIL(2);
        int value;

        PayState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum OrderType{
        SINGLE(0),GROUP_OWNER(1),GROUP_FLLOWER(2);
        int value;

        OrderType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum RefundState {
        NONE(0),REFUND(1),REFUND_SUCESS(2),REFUND_FILED(3);
        int value;

        RefundState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private int id;
    private String orderId;
    private int groupActivityId;
    private String unionId;
    private double price;
    private int type;

    private int payState;
    private int refundState;
    private String ptId;

    private Date createTime;
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(int groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
