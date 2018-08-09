package com.longwei.umier.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付相关配置
 * <p>
 * Created by bjliumingbo on 2017/5/12.
 */
@Configuration
public class WxPayConfiguration {
	@Value("${wx.pay.appId}")
	private String appId;

	@Value("${wx.pay.mchId}")
	private String mchId;

	@Value("${wx.pay.mchKey}")
	private String mchKey;

	@Value("${wx.pay.subAppId}")
	private String subAppId;

	@Value("${wx.pay.subMchId}")
	private String subMchId;

	@Value("${wx.pay.keyPath}")
	private String keyPath;

	@Bean
	public WxPayConfig payConfig() {
		WxPayConfig payConfig = new WxPayConfig();
		payConfig.setAppId(this.appId);
		payConfig.setMchId(this.mchId);
		payConfig.setMchKey(this.mchKey);
		payConfig.setSubAppId(this.subAppId);
		payConfig.setSubMchId(this.subMchId);
		payConfig.setKeyPath(this.keyPath);

		return payConfig;
	}

	@Bean
	public WxPayService payService() {
		WxPayService payService = new WxPayServiceImpl();
		payService.setConfig(payConfig());
		return payService;
	}
}
