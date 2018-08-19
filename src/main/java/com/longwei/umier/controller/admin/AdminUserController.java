package com.longwei.umier.controller.admin;

import com.longwei.umier.service.JWTService;
import com.longwei.umier.utils.*;
import com.longwei.umier.vo.AdminUserLoginVo;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api/v1/admin-user")
public class AdminUserController {

    @Value("${admin.user}")
    private String adminUser;

    @Value("${admin.passwd}")
    private String adminPasswd;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public void loginAdmin(@RequestBody AdminUserLoginVo adminUserLoginVo, HttpServletResponse response) {
        String enryptPasswd = MD5Util.md5Encode(adminPasswd, "UTF-8");
        if (adminUser.equals(adminUserLoginVo.getUsername())
                && enryptPasswd.equals(adminUserLoginVo.getPasswd())) {
            String token = UUID.randomUUID().toString();


        } else {

        }


    }

}
