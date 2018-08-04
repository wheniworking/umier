package com.longwei.umier.controller.wx;

import com.longwei.umier.entity.WxMp;
import com.longwei.umier.service.OAuth2Service;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/api/v1/wxmp/oauth2")
public class OAuth2Controller {

    @Autowired
    private OAuth2Service oAuth2Service;

    @GetMapping
    public String oauth(@RequestParam String page){
        String url = oAuth2Service.buildOAuth2Url(page);
        return "redirect:" + url;
    }

    @GetMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response ) throws WxErrorException, IOException {
        oAuth2Service.oauth(request, response);
    }
}
