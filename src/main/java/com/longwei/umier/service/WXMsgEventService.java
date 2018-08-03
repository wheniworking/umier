package com.longwei.umier.service;

import com.longwei.umier.handler.SubscribeHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class WXMsgEventService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private SubscribeHandler subscribeHandler;

    private WxMpMessageRouter router;

    @PostConstruct
    public  void init() {
            router = new WxMpMessageRouter(wxMpService);
            //配置转发规则
            router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                    .event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler)
                    .end();
    }

    public void handleEvent(HttpServletRequest request, HttpServletResponse response ) {
        //处理微信的事件
        //完成相关数据的保存即可
//        WxMpMessageRouter router = new WxMpMessageRouter();

        WxMpXmlMessage inMessage = null;
        WxMpXmlOutMessage outMessage = null;
        try {
            inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            outMessage = router.route(inMessage);
            response.getWriter().write(outMessage.toXml());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
