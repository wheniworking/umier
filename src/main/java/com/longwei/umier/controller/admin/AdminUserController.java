package com.longwei.umier.controller.admin;

import com.longwei.umier.service.SessionContext;
import com.longwei.umier.utils.*;
import com.longwei.umier.vo.AdminUserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminUserController {

    @Value("${admin.user}")
    private String adminUser;

    @Value("${admin.passwd}")
    private String adminPasswd;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/login")
    public DataMap login(@RequestBody AdminUserLoginVo adminUserLoginVo, HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<>();
        String enryptPasswd = MD5Util.md5Encode(adminPasswd, "UTF-8");
        if (adminUser.equals(adminUserLoginVo.getUsername())
                && enryptPasswd.equals(adminUserLoginVo.getPasswd())) {
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("umier:admin:auth:"+token, adminUser, 24, TimeUnit.HOURS);
            ret.put("token", token);
            ret.put("username", adminUser);
            return ResponseBuilder.create().ok().data(ret).build();
        } else {
            return ResponseBuilder.create().error(StatusCode.AUTH_ERROR).build();
        }


    }

    @PostMapping("/logout")
    public DataMap logout(HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        redisTemplate.delete("umier:admin:auth:"+token);
        return ResponseBuilder.create().ok().build();
    }

}
