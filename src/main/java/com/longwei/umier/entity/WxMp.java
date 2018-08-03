package com.longwei.umier.entity;

import java.util.Date;

public class WxMp {
    private String unionId;
    private String openId;
    private int type;
    private Date createTime;
    private int state;

    public static enum WxMpType {
        MP(0),MINIAPP(1);
        int value;

        private WxMpType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
