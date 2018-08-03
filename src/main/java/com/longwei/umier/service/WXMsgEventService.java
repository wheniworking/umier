package com.longwei.umier.service;

import com.longwei.umier.handler.LogHandler;
import com.longwei.umier.handler.MsgHandler;
import com.longwei.umier.handler.SubscribeHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Service
public class WXMsgEventService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    private SubscribeHandler subscribeHandler;

    @Autowired
    private LogHandler logHandler;

    @Autowired
    private MsgHandler msgHandler;

    private WxMpMessageRouter router;

    @PostConstruct
    public void init() {
        router = new WxMpMessageRouter(wxMpService);
        router.rule().handler(this.logHandler).next();
        //配置转发规则
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler)
                .end();
        // 默认,转发消息给客服人员
        router.rule().async(false).handler(this.msgHandler).end();
    }

    public void handleEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!this.wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echoStr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            String echoStrOut = String.copyValueOf(echoStr.toCharArray());
            response.getWriter().println(echoStrOut);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type"))
                ? "raw"
                : request.getParameter("encrypt_type");
        switch (encryptType) {
            case "raw":
                response.getWriter().println(processRawMsg(request.getInputStream()));
                break;
            case "aes":
                String msgSignature = request.getParameter("msg_signature");
                response.getWriter().println(processEncryptedMsg(request.getInputStream(), timestamp ,nonce, msgSignature));
                break;
            default:
                response.getWriter().println("不可识别的加密类型");
        }
    }

    private String processRawMsg(InputStream inputStream) {
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(inputStream);
        WxMpXmlOutMessage outMessage  = router.route(inMessage);
        if (outMessage == null) {
            return "";
        } else {
            return outMessage.toXml();
        }
    }

    private String processEncryptedMsg(InputStream inputStream , String timestamp, String nonce, String msgSignature ) {
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                inputStream, this.wxMpConfigStorage, timestamp, nonce,
                msgSignature);
        WxMpXmlOutMessage outMessage  = router.route(inMessage);
        if (outMessage == null) {
            return "";
        } else {
            return outMessage.toEncryptedXml(this.wxMpConfigStorage);
        }
    }
}
