package com.longwei.umier.interceptor;

import com.longwei.umier.vo.WxMpUserInfoVo;

public class WxAuthInfoHolder {

    private static ThreadLocal<WxMpUserInfoVo> threadLocal = new ThreadLocal<>();

    public static void init(WxMpUserInfoVo wxMpUserInfoVo) {
        threadLocal.set(wxMpUserInfoVo);
    }

    public static WxMpUserInfoVo get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }


}
