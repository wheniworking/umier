package com.longwei.umier.controller.admin;

import com.longwei.umier.service.SessionContext;
import com.longwei.umier.utils.*;
import com.longwei.umier.vo.AdminUserLoginVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminUserController {

    @Value("${admin.user}")
    private String adminUser;

    @Value("${admin.passwd}")
    private String adminPasswd;

    @PostMapping("/login")
    public DataMap login(@RequestBody AdminUserLoginVo adminUserLoginVo, HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<>();
        String enryptPasswd = MD5Util.md5Encode(adminPasswd, "UTF-8");
        if (adminUser.equals(adminUserLoginVo.getUsername())
                && enryptPasswd.equals(adminUserLoginVo.getPasswd())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", adminUser);
            SessionContext.addSession(session.getId(), session);
            ret.put("token", session.getId());
            ret.put("username", session.getAttribute("username"));
            return ResponseBuilder.create().ok().data(ret).build();
        } else {
            return ResponseBuilder.create().error(StatusCode.AUTH_ERROR).build();
        }


    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        HttpSession session = SessionContext.getSession(token);
        session.invalidate();
        SessionContext.delSession(token);
    }

}
