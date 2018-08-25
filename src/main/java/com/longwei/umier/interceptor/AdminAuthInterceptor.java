package com.longwei.umier.interceptor;

import com.longwei.umier.service.SessionContext;
import com.longwei.umier.utils.BaseException;
import com.longwei.umier.utils.StatusCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AdminAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("x-auth-token");
        if (StringUtils.isBlank(token)) {
            throw new BaseException(StatusCode.AUTH_ERROR);
        }
        String username = redisTemplate.opsForValue().get("umier:admin:auth:" + token);

        if (username == null) {
            throw new BaseException(StatusCode.AUTH_ERROR);
        }
        return super.preHandle(request, response, handler);
    }
}
