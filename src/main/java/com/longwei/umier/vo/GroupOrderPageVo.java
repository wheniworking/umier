package com.longwei.umier.vo;

import java.util.Date;
import java.util.List;

public class GroupOrderPageVo {
    private int count;
    private int pageSize;
    private int currentPage;
    private List<GroupOrderVo> vos;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<GroupOrderVo> getVos() {
        return vos;
    }

    public void setVos(List<GroupOrderVo> vos) {
        this.vos = vos;
    }
}
