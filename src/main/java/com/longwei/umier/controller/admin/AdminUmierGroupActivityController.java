package com.longwei.umier.controller.admin;

import com.longwei.umier.entity.CourseGroupActivity;
import com.longwei.umier.service.AdminGroupActivityService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import com.longwei.umier.vo.CourseGroupActivityPageVo;
import com.longwei.umier.vo.CourseGroupActivityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/groupActivity")
public class AdminUmierGroupActivityController {

    @Autowired
    private AdminGroupActivityService adminGroupActivityService;

    /**
     * 创建拼团活动
     * @return
     */
    @PostMapping
    public DataMap createGroupActivity(@RequestBody CourseGroupActivity activity){
        adminGroupActivityService.createGroupActivity(activity);
        return ResponseBuilder.create().ok().build();
    }

    @GetMapping
    public DataMap getGroupActivity(@RequestParam  int page,@RequestParam int size) {
        CourseGroupActivityPageVo vo = adminGroupActivityService.getGroupActivity(page, size);
        return ResponseBuilder.create().ok().data(vo).build();
    }

    @GetMapping("/orders")
    public DataMap getGroupActivityOrders(@RequestParam  int page,@RequestParam int size) {
        return ResponseBuilder.create().ok().data(adminGroupActivityService.getGroupOrders(page, size)).build();
    }

    @GetMapping("/orders/{ptId}")
    public DataMap getGroupActivityOrderDetail(@PathVariable String ptId) {
        return ResponseBuilder.create().ok().data(adminGroupActivityService.getGroupOrder(ptId)).build();

    }
}
