package com.longwei.umier.dao;

import com.longwei.umier.entity.CourseGroupActivity;
import com.longwei.umier.entity.CourseGroupActivityOrder;
import com.longwei.umier.vo.CourseGroupActivityGroupCountVo;
import com.longwei.umier.vo.CourseGroupActivityVo;
import com.longwei.umier.vo.GroupOrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseGroupActivityOrderDao {

    List<CourseGroupActivityGroupCountVo> batchCountGroups(@Param("activityIds") List<Integer> activityIds);

    int countGroupOrder();

    List<GroupOrderVo> getGroupOrders(@Param("index") int index, @Param("size") int size);

    List<GroupOrderVo> getParticipateGroupOrders(@Param("ptIds") List<String> ptIds);

    List<GroupOrderVo> getGroupOrdersByOrderId(@Param("ptId")String ptId);

    List<GroupOrderVo> getGroupByActivityId(@Param("activityId")int activityId);

    void insert(CourseGroupActivityOrder order);
}
