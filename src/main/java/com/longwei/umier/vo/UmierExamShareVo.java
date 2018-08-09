package com.longwei.umier.vo;

import com.longwei.umier.entity.UmierUserExamRecord;

public class UmierExamShareVo {
    public boolean isSameUser;
    public UmierUserExamRecord record;

    public UmierExamShareVo() {
    }

    public UmierExamShareVo(boolean isSameUser, UmierUserExamRecord record) {
        this.isSameUser = isSameUser;
        this.record = record;
    }

    public boolean isSameUser() {
        return isSameUser;
    }

    public void setSameUser(boolean sameUser) {
        isSameUser = sameUser;
    }

    public UmierUserExamRecord getRecord() {
        return record;
    }

    public void setRecord(UmierUserExamRecord record) {
        this.record = record;
    }
}
