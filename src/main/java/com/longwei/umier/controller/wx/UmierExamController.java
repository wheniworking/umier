package com.longwei.umier.controller.wx;

import com.longwei.umier.service.UmierExamService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import com.longwei.umier.vo.UmierExamUserRetVo;
import com.longwei.umier.vo.UmierUserAnswerVo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exam")
public class UmierExamController {

    @Value("${wx.mp.exam.static.page.url}")
    private String examRedirectUrl;
    @Value("${wx.mp.exam.static.share.page.url}")
    private String shareExamRedirectUrl;
    private final UmierExamService umierExamService;

    private final WxMpService wxMpService;

    @Autowired
    public UmierExamController(UmierExamService umierExamService, WxMpService wxMpService) {
        this.umierExamService = umierExamService;
        this.wxMpService = wxMpService;
    }

    /**
     * 打开分享的测评页面
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/page/{shareId}")
    public void getSharePage(@PathVariable("shareId")String shareId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format(shareExamRedirectUrl, shareId));
    }

    /**
     * 打开测评页面
     * @param request
     * @param response
     * @throws WxErrorException
     * @throws IOException
     */
    @GetMapping("/page")
    public void getExamPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //重定向到约定地址
        response.sendRedirect(examRedirectUrl);
    }

    @GetMapping
    public DataMap getExams() {
        return ResponseBuilder.create().ok().data(umierExamService.getExams()).build();
    }

    @GetMapping("/questions")
    public DataMap getExamQuestions() {
        return ResponseBuilder.create().ok().data(umierExamService.selectQuestions()).build();
    }

    @PostMapping("/submit")
    public DataMap submitExam(@RequestBody UmierUserAnswerVo userAnswer) {
        UmierExamUserRetVo ret = umierExamService.submitExam(userAnswer);
        return ResponseBuilder.create().ok().data(ret).build();
    }

    @GetMapping("/page/share/{shareId}")
    @ResponseBody
    public DataMap getPageShareData(@PathVariable String shareId){
        return ResponseBuilder.create().ok().data(umierExamService.getPageShareData(shareId)).build();
    }

    @Value("${wx.appId}")
    private String appId;
    @GetMapping("/share/ticket")
    public DataMap getTicket(@RequestParam("url")String url) throws WxErrorException {
        String ticket = wxMpService.getJsapiTicket();
        String noncestr = RandomStringUtils.randomAlphabetic(8);
        long timestamp = System.currentTimeMillis();
        String str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", ticket, noncestr, timestamp, url);
        String sign = DigestUtils.sha1Hex(str);
        System.out.println(str);
        Map<String, Object> map = new HashMap<>();
        map.put("noncestr", noncestr);
        map.put("ticket", ticket);
        map.put("ts", timestamp);
        map.put("sign", sign);
        map.put("appId", appId);
        return ResponseBuilder.create().ok().data(map).build();
    }

}
