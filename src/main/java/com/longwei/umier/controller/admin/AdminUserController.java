package com.longwei.umier.controller.admin;

import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/admin-user")
public class AdminUserController {

    @PostMapping("/login")
    public DataMap loginAdmin() {

        return ResponseBuilder.create().ok().build();
    }

}
