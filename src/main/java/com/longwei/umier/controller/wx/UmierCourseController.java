package com.longwei.umier.controller.wx;

import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/course")
public class UmierCourseController {

    @GetMapping()
    public DataMap getCourse() {
        return ResponseBuilder.create().ok().build();
    }
}
