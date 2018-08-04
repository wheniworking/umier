package com.longwei.umier.dao;

import com.longwei.umier.entity.WxMp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WxMpDao {

    void insert(WxMp wxMp);

    WxMp getByOpenId(@Param("openId") String openId);
}
