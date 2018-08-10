package com.longwei.umier.service;

import com.longwei.umier.dao.CourseGroupActivityDao;
import com.longwei.umier.dao.CourseGroupActivityOrderDao;
import com.longwei.umier.dao.WxUserDao;
import com.longwei.umier.entity.CourseGroupActivity;
import com.longwei.umier.entity.WxUser;
import com.longwei.umier.utils.DateUtils;
import com.longwei.umier.vo.ActivityInfoVo;
import com.longwei.umier.vo.GroupOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupActivityService {

    @Autowired
    private CourseGroupActivityDao courseGroupActivityDao;

    @Autowired
    private CourseGroupActivityOrderDao courseGroupActivityOrderDao;
    @Autowired
    private WxUserDao wxUserDao;

    public Object getActivity(int id) {
        CourseGroupActivity activity = courseGroupActivityDao.getById(id);
        List<GroupOrderVo> groupOrderVos = courseGroupActivityOrderDao.getGroupByActivityId(id);
        List<String> ptIds = groupOrderVos.stream().map(GroupOrderVo::getPtId).collect(Collectors.toList());
        List<GroupOrderVo> participateGroupOrders = courseGroupActivityOrderDao.getParticipateGroupOrders(ptIds);
        Map<String, List<GroupOrderVo>> participateGroupMap =
                participateGroupOrders.stream().collect(Collectors.groupingBy(GroupOrderVo::getPtId));

        List<String> unionIds = groupOrderVos.stream().map(GroupOrderVo::getUnionId).collect(Collectors.toList());
        List<WxUser> wxUsers = wxUserDao.getUserByUnionIds(unionIds);
        Map<String, WxUser> wxUserMap = wxUsers.stream().collect(Collectors.toMap(WxUser::getUnionId, r -> r));
        for (GroupOrderVo vo : groupOrderVos) {
            List<GroupOrderVo> participates = participateGroupMap.get(vo.getPtId());
            int size = participates == null ? 0 : participates.size();
            vo.setPtNum(activity.getPtNum());
            vo.setDiffNum(activity.getPtNum() - size);
            vo.setLeftTime(DateUtils.diffTime(new Date(), DateUtils.addHour(vo.getCreateTime(), activity.getPtPeriod())));
            WxUser u = wxUserMap.get(vo.getUnionId());
            if (u != null) {
                vo.setNickName(u.getNickname());
                vo.setAvatar(u.getAvatar());
            }
        }

        ActivityInfoVo vo = new ActivityInfoVo();
        vo.setActivity(activity);
        vo.setGroupOrderVos(groupOrderVos);
        return vo;

    }

    public Object getGroupUserInfo(String ptId) {
        List<GroupOrderVo> groupOrderVos = courseGroupActivityOrderDao.getGroupOrdersByOrderId(ptId);
        List<String> unionIds = groupOrderVos.stream().map(GroupOrderVo::getUnionId).collect(Collectors.toList());
        return wxUserDao.getUserByUnionIds(unionIds);
    }
}
