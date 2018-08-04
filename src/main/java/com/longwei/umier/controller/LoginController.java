package com.longwei.umier.controller;

import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @PostMapping("/login")
    public DataMap login() {
        return ResponseBuilder.create().ok().build();
    }
}
