package com.longwei.umier.interceptor;

import com.longwei.umier.service.SessionContext;
import com.longwei.umier.utils.BaseException;
import com.longwei.umier.utils.StatusCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminAuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (StringUtils.isBlank(token)) {
            throw new BaseException(StatusCode.AUTH_ERROR);
        }
        HttpSession session = SessionContext.getSession(token);
        if (session == null) {
            throw new BaseException(StatusCode.AUTH_ERROR);
        }
        return super.preHandle(request, response, handler);
    }
}
