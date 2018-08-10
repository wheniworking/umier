package com.longwei.umier.dao;

import com.longwei.umier.entity.CourseGroupActivity;
import com.longwei.umier.vo.CourseGroupActivityVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseGroupActivityDao {
    void insert(CourseGroupActivity activity) ;

    List<CourseGroupActivityVo> getGroupActivity(@Param("index") int index, @Param("size") int size);

    int count();

    List<CourseGroupActivityVo> getByIds(@Param("ids") List<Integer> activityIds);

    CourseGroupActivity getById(@Param("id") int activityId);
}
