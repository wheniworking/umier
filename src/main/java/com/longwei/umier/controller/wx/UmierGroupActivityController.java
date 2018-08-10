package com.longwei.umier.controller.wx;

import com.longwei.umier.service.GroupActivityService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/v1/groupActivity")
public class UmierGroupActivityController {


    @Autowired
    private GroupActivityService groupActivityService;

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

}
