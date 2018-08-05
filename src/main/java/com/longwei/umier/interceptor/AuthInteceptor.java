package com.longwei.umier.interceptor;

import com.longwei.umier.service.JWTService;
import com.longwei.umier.vo.WxMpUserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInteceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JWTService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WxMpUserInfoVo wxMpUserInfoVo = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("x-auth-token".equals(cookie.getName())) {
                wxMpUserInfoVo = jwtService.decryptJWTToken(cookie.getValue());
            }
        }

        return wxMpUserInfoVo != null;

    }
}
