package com.longwei.umier.dao;

import com.longwei.umier.entity.WxMp;
import com.longwei.umier.entity.WxUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxUserDao {

    List<WxUser> getUserByUnionIds(@Param("unionIds")List<String> unionIds);

    void insert(WxUser user);
}
