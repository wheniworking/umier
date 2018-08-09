package com.longwei.umier.controller.wx;

import com.longwei.umier.service.UmierExamService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import com.longwei.umier.vo.UmierExamUserRetVo;
import com.longwei.umier.vo.UmierUserAnswerVo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/exam")
public class UmierExamController {

    @Value("${wx.mp.exam.static.page.url}")
    private String examRedirectUrl;
    @Value("${wx.mp.exam.static.share.page.url}")
    private String shareExamRedirectUrl;
    private final UmierExamService umierExamService;

    @Autowired
    public UmierExamController(UmierExamService umierExamService) {
        this.umierExamService = umierExamService;
    }

    /**
     * 打开分享的测评页面
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/page/{shareId}")
    public void getSharePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(shareExamRedirectUrl);
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

    @GetMapping("/{examId}/questions")
    public DataMap getExamQuestions(@PathVariable("examId") int examId) {
        return ResponseBuilder.create().ok().data(umierExamService.selectQuestions(examId)).build();
    }

    @PostMapping("/{examId}/submit")
    public DataMap submitExam(@RequestBody UmierUserAnswerVo userAnswer) {
        UmierExamUserRetVo ret = umierExamService.submitExam(userAnswer);
        return ResponseBuilder.create().ok().data(ret).build();
    }

    @GetMapping("/page/share/{shareId}")
    @ResponseBody
    public DataMap getPageShareData(@PathVariable String shareId){
        return ResponseBuilder.create().ok().data(umierExamService.getPageShareData(shareId)).build();
    }
}
