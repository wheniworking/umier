package com.longwei.umier.service;

import com.longwei.umier.dao.CourseGroupActivityDao;
import com.longwei.umier.dao.CourseGroupActivityOrderDao;
import com.longwei.umier.dao.WxUserDao;
import com.longwei.umier.entity.CourseGroupActivity;
import com.longwei.umier.entity.WxUser;
import com.longwei.umier.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminGroupActivityService {

    @Autowired
    private CourseGroupActivityDao courseGroupActivityDao;

    @Autowired
    private CourseGroupActivityOrderDao courseGroupActivityOrderDao;

    @Autowired
    private WxUserDao wxUserDao;

    public void createGroupActivity(CourseGroupActivity courseGroupActivity) {
        courseGroupActivityDao.insert(courseGroupActivity);
    }

    public CourseGroupActivityPageVo getGroupActivity(int page, int size) {
        int cnt = courseGroupActivityDao.count();
        List<CourseGroupActivityVo> activityVos = courseGroupActivityDao.getGroupActivity(page * size, size);
        List<Integer> activityIds = activityVos.stream().map(CourseGroupActivityVo::getId).collect(Collectors.toList());
        List<CourseGroupActivityGroupCountVo> countVos = courseGroupActivityOrderDao.batchCountGroups(activityIds);
        Map<Integer, CourseGroupActivityGroupCountVo> map = countVos.stream().collect(Collectors.toMap(CourseGroupActivityGroupCountVo::getActivityId, r -> r));
        for (CourseGroupActivityVo vo : activityVos) {
            CourseGroupActivityGroupCountVo cv = map.get(vo.getId());
            if (cv != null) {
                vo.setGroupNum(cv.getCnt());
            }
        }
        CourseGroupActivityPageVo vo = new CourseGroupActivityPageVo();
        vo.setCount(cnt);
        vo.setVos(activityVos);
        vo.setPageSize(size);
        vo.setCurrentPage(page);
        return vo;
    }

    public GroupOrderPageVo getGroupOrders(int page, int size) {
        int cnt = courseGroupActivityOrderDao.countGroupOrder();
        List<GroupOrderVo> groupOrderVos = courseGroupActivityOrderDao.getGroupOrders(page * size, size);
        List<Integer> activityIds = groupOrderVos.stream().map(GroupOrderVo::getActivityId).collect(Collectors.toList());
        List<CourseGroupActivityVo> activityVos = courseGroupActivityDao.getByIds(activityIds);
        Map<Integer,CourseGroupActivityVo> activityMap = activityVos.stream().collect(Collectors.toMap(CourseGroupActivityVo::getId, r -> r));
        for (GroupOrderVo vo : groupOrderVos) {
            CourseGroupActivityVo cgav = activityMap.get(vo.getActivityId());
            if (cgav != null) {
                vo.setActivityImageUrl(cgav.getImageUrl());
                vo.setActivityTitle(cgav.getTitle());
                vo.setPtNum(cgav.getPtNum());
                vo.setStartTime(cgav.getStartTime());
                vo.setEndTime(cgav.getEndTime());
            }
        }
        List<String> unionIds = groupOrderVos.stream().map(GroupOrderVo::getUnionId).collect(Collectors.toList());

        List<WxUser> wxUsers = wxUserDao.getUserByUnionIds(unionIds);
        Map<String,WxUser> wxUserMap = wxUsers.stream().collect(Collectors.toMap(WxUser::getUnionId, w -> w));

        for (GroupOrderVo vo : groupOrderVos) {
            WxUser u = wxUserMap.get(vo.getUnionId());
            if (u != null) {
                vo.setNickName(u.getNickname());
            }
        }
        List<String> ptIds = groupOrderVos.stream().map(GroupOrderVo::getOrderId).collect(Collectors.toList());

        List<GroupOrderVo> participateGroupOrderVos = courseGroupActivityOrderDao.getParticipateGroupOrders(ptIds);
        Map<String, List<GroupOrderVo>> participateGroupMap =
                participateGroupOrderVos.stream().collect(Collectors.groupingBy(GroupOrderVo::getParentOrderId));

        for (GroupOrderVo vo : groupOrderVos) {
            List<GroupOrderVo> g = participateGroupMap.get(vo.getOrderId());
            int currentSize = g == null ? 0 : g.size();
            vo.setDiffNum(vo.getPtNum() - currentSize);
        }

        GroupOrderPageVo vo = new GroupOrderPageVo();
        vo.setCount(cnt);
        vo.setCurrentPage(page);
        vo.setPageSize(size);
        vo.setVos(groupOrderVos);

        return vo;
    }

    public GroupActivityOrderDetailVo getGroupOrder(String ptId) {
        List<GroupOrderVo> groupOrderVos = courseGroupActivityOrderDao.getGroupOrdersByOrderId(ptId);
        Optional<GroupOrderVo> opt = groupOrderVos.stream().filter(it -> it.getType() == 1).findFirst();
        GroupOrderVo starter = null;
        if(opt.isPresent()) {
            starter = opt.get();
        } else {
            return null;
        }
        CourseGroupActivity activity = courseGroupActivityDao.getById(starter.getActivityId());
        List<String> unionIds = groupOrderVos.stream().map(GroupOrderVo::getUnionId).collect(Collectors.toList());
        List<WxUser> wxusers = wxUserDao.getUserByUnionIds(unionIds);

        GroupActivityOrderDetailVo vo = new GroupActivityOrderDetailVo();
        vo.setPtId(ptId);
        vo.setState(starter.getState());
        vo.setStartTime(starter.getStartTime());
        vo.setEndTime(starter.getEndTime());

        vo.setActivityStartTime(activity.getStartTime());
        vo.setActivityEndTime(activity.getEndTime());
        vo.setActivityId(activity.getId());
        vo.setImageUrl(activity.getImageUrl());
        vo.setTitle(activity.getTitle());
        vo.setPtNum(activity.getPtNum());
        vo.setSinglePrice(activity.getSinglePrice());
        vo.setGroupPrice(activity.getGroupPrice());

        vo.setParticipaters(wxusers);
        return vo;


    }
}
