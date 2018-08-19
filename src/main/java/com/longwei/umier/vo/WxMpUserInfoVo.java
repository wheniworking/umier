package com.longwei.umier.vo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class WxMpUserInfoVo {
    private Boolean subscribe;
    private String openId;
    private String nickname;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headImgUrl;
    private Long subscribeTime;
    private String unionId;

    public WxMpUserInfoVo() {
    }

    public WxMpUserInfoVo(WxMpUser wxMpUser) {
        this.subscribe = wxMpUser.getSubscribe();
        this.openId = wxMpUser.getOpenId();
        this.nickname = wxMpUser.getNickname();
        this.sex = wxMpUser.getSex();
        this.language = wxMpUser.getLanguage();
        this.city = wxMpUser.getCity();
        this.province = wxMpUser.getProvince();
        this.country = wxMpUser.getCountry();
        this.headImgUrl = wxMpUser.getHeadImgUrl();
        this.subscribeTime = wxMpUser.getSubscribeTime();
        this.unionId = wxMpUser.getUnionId();
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(this);
                if(obj!= null) {
                    map.put(field.getName(), obj);
                }
            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static WxMpUserInfoVo parse(Map<String, Object> map) {
        WxMpUserInfoVo wxMpUserInfoVo = new WxMpUserInfoVo();
        try {
            for (Map.Entry<String, Object> e : map.entrySet()) {
                try {
                    Field field = wxMpUserInfoVo.getClass().getDeclaredField(e.getKey());
                    field.setAccessible(true);
                    field.set(wxMpUserInfoVo, e.getValue());
                } catch (Exception e1) {

                }
            }
        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        }
        return wxMpUserInfoVo;
    }
}
