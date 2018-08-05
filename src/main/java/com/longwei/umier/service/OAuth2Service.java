package com.longwei.umier.service;

import com.longwei.umier.dao.WxMpDao;
import com.longwei.umier.entity.WxMp;
import com.longwei.umier.vo.WxMpUserInfoVo;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class OAuth2Service {

    @Value("${wx.mp.oauth.url}")
    private String oauthUrl ;
    @Value("${wx.mp.static.page.url}")
    private String wxMpStaticPage ;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpDao wxMpDao;

    @Autowired
    private JWTService jwtService;


    public String buildOAuth2Url(String page) {
        return wxMpService.oauth2buildAuthorizationUrl(oauthUrl,
                WxConsts.OAuth2Scope.SNSAPI_BASE, page);
    }

    public void oauth(HttpServletRequest request, HttpServletResponse response ) throws WxErrorException, IOException {
        String code = request.getParameter("code");
        String page = request.getParameter("state");
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

        //重定向到约定地址
        response.sendRedirect(String.format(wxMpStaticPage, page));
    }
}
