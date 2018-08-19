package com.longwei.umier.controller.wx;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.util.SignUtils;
import com.longwei.umier.service.GroupActivityService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import com.longwei.umier.utils.XMLUtil;
import com.longwei.umier.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/groupActivity")
public class UmierGroupActivityController {


    @Autowired
    private GroupActivityService groupActivityService;
    @Autowired
    private WxPayConfig payConfig;

    @GetMapping()
    public void getCourse(HttpServletRequest request, HttpServletResponse response) {
//        response.sendRedirect();
    }

    @GetMapping("/courseInfo")
    public DataMap getCourseInfo(@RequestParam int id) {
        return ResponseBuilder.create().ok().data(groupActivityService.getActivity(id)).build();
    }

    @GetMapping("/{ptId}/users")
    public DataMap getGroupUserInfo(@PathVariable String ptId) {
        return ResponseBuilder.create().ok().data(groupActivityService.getGroupUserInfo(ptId)).build();
    }

    /**
     * 保存订单信息，返回微信预支付参数给前端
     * @param orderVo
     * @return
     */
    @PostMapping("/order")
    public DataMap order(@RequestBody OrderVo orderVo) {
        return ResponseBuilder.create().ok().data(groupActivityService.orderCourse(orderVo)).build();
    }

}
