package com.longwei.umier.vo;

import com.longwei.umier.entity.CourseGroupActivity;

import java.util.List;

public class ActivityInfoVo {
    private CourseGroupActivity activity;
    private List<GroupOrderVo> groupOrderVos;

    public CourseGroupActivity getActivity() {
        return activity;
    }

    public void setActivity(CourseGroupActivity activity) {
        this.activity = activity;
    }

    public List<GroupOrderVo> getGroupOrderVos() {
        return groupOrderVos;
    }

    public void setGroupOrderVos(List<GroupOrderVo> groupOrderVos) {
        this.groupOrderVos = groupOrderVos;
    }
}
