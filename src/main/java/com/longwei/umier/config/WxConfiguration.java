package com.longwei.umier.config;

import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfiguration {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;

    @Value("${wx.token}")
    private String token;

    @Value("${wx.aesKey}")
    private String aesKey;

    @Bean
    public WxMpConfigStorage wxMpInMemoryConfigStorage() {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(appSecret); // 设置微信公众号的app corpSecret
        config.setToken(token); // 设置微信公众号的token
        config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey
        return config;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpInMemoryConfigStorage());
        return wxMpService;
    }


}

