package com.longwei.umier.controller.wx;

import com.longwei.umier.service.WXMsgEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/wx")
public class WXMsgEventController {

    @Autowired
    private WXMsgEventService wxMsgEventService;

    @GetMapping(value = "/msg")
    public String connectWechat(@RequestParam("echostr") String echoStr) {
        return echoStr;
    }

    @PostMapping(value = "/msg")
    public void receiveEvent(HttpServletRequest request, HttpServletResponse response ) throws IOException {
        wxMsgEventService.handleEvent(request, response);
    }

}
