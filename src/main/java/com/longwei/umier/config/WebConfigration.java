package com.longwei.umier.config;

import com.longwei.umier.dao.WxMpDao;
import com.longwei.umier.interceptor.AuthInteceptor;
import com.longwei.umier.service.JWTService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfigration  extends WebMvcConfigurerAdapter{


    @Autowired
    private WxMpDao wxMpDao;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private JWTService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthInteceptor(jwtService, wxMpService, wxMpDao)).
                addPathPatterns("/api/v1/exam/**");
        super.addInterceptors(registry);

    }
}
