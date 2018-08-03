package com.longwei.umier.handler;


import com.longwei.umier.dao.WxMpDao;
import com.longwei.umier.entity.WxMp;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 用户关注公众号Handler
 */
@Component
public class SubscribeHandler extends AbstractWxMessageHandler {

    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService wxMpService;
    @Autowired
    private WxMpDao wxMpDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                    WxMpService wxMpService, WxSessionManager sessionManager) {
        WxMpUser wxMpUser = getUserInfo(wxMessage.getFromUser(), "zh_CN");
        WxMp wxMp = new WxMp();
        wxMp.setUnionId(wxMpUser.getUnionId());
        wxMp.setOpenId(wxMessage.getFromUser());
        wxMp.setType(WxMp.WxMpType.MP.getValue());
        wxMp.setCreateTime(new Date());
        wxMp.setState(0);
        wxMpDao.insert(wxMp);
        WxMpXmlOutTextMessage m
            = WxMpXmlOutMessage.TEXT()
            .content("尊敬的" + wxMpUser.getNickname() + "，您好！")
            .fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser())
            .build();
        logger.info("subscribeMessageHandler" + m.getContent());
        return m;
    }

    private WxMpUser getUserInfo(String openid, String lang) {
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = this.wxMpService.getUserService().userInfo(openid, lang);
        } catch (WxErrorException e) {
            this.logger.error(e.getError().toString());
        }
        return wxMpUser;
    }
}
