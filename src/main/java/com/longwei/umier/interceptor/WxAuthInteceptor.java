package com.longwei.umier.interceptor;

import com.longwei.umier.dao.WxMpDao;
import com.longwei.umier.entity.WxMp;
import com.longwei.umier.service.JWTService;
import com.longwei.umier.vo.WxMpUserInfoVo;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class WxAuthInteceptor extends HandlerInterceptorAdapter {

    private JWTService jwtService;

    private WxMpService wxMpService;

    private WxMpDao wxMpDao;

    public WxAuthInteceptor(JWTService jwtService,
                            WxMpService wxMpService,
                            WxMpDao wxMpDao) {
        this.jwtService = jwtService;
        this.wxMpService = wxMpService;
        this.wxMpDao = wxMpDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WxMpUserInfoVo wxMpUserInfoVo = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("x-auth-token".equals(cookie.getName())) {
                    wxMpUserInfoVo = jwtService.decryptJWTToken(cookie.getValue());
                    WxAuthInfoHolder.init(wxMpUserInfoVo);
                }
            }
        }

        if (wxMpUserInfoVo == null) {
            String code = request.getParameter("code");
            if (StringUtils.isBlank(code)) {
                String requestUri = request.getRequestURL().toString();
                String redirectUrl = buildOAuth2Url(requestUri);
                response.sendRedirect(redirectUrl);
                return false;
            } else {
                oauth(request, response);

                return true;
            }
        }

        return true;
    }

    private String buildOAuth2Url(String url) {
        return wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
    }

    private void oauth(HttpServletRequest request, HttpServletResponse response ) throws WxErrorException, IOException {
        String code = request.getParameter("code");
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(wxMpOAuth2AccessToken.getOpenId(), "zh_CN");
        WxMp wxMp = new WxMp();
        wxMp.setUnionId(wxMpUser.getUnionId());
        wxMp.setOpenId(wxMpUser.getOpenId());
        wxMp.setType(WxMp.WxMpType.MP.getValue());
        wxMp.setCreateTime(new Date());
        wxMp.setState(0);
        wxMpDao.insert(wxMp);

        //生成jwt token下发到浏览器中
        String jws = jwtService.generateJWTToken(new WxMpUserInfoVo(wxMpUser));
        Cookie cookie = new Cookie("x-auth-token", jws);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        WxAuthInfoHolder.init(new WxMpUserInfoVo(wxMpUser));
        //重定向到约定地址
//        response.sendRedirect(String.format(wxMpStaticPage, page));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        WxAuthInfoHolder.clear();
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WxAuthInfoHolder.clear();
        super.afterCompletion(request, response, handler, ex);
    }
}
