package com.longwei.umier.vo;

import java.util.List;

public class CourseGroupActivityPageVo {
    private int count;
    private int pageSize;
    private int currentPage;
    private List<CourseGroupActivityVo> vos;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CourseGroupActivityVo> getVos() {
        return vos;
    }

    public void setVos(List<CourseGroupActivityVo> vos) {
        this.vos = vos;
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
}
