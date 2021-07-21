package org.cloud.userservice.controller;

import com.cloud.common.pojo.User;
import org.churunfa.security.grant.auth.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    private Map login(@RequestBody User user) {
        return authService.login(user.getUsername(), user.getPassword());
    }

    @Login
    @PostMapping("/check_login")
    private Map check(HttpServletRequest request) {
        String jwt_token = request.getHeader("Authorization");
        return authService.check(jwt_token);
    }

    @PostMapping("/register")
    private Map register(@RequestBody User user) {
        HashMap<String, Object> map = new HashMap<>();
        if (user.getUsername() == null || user.getPassword() == null) {
            map.put("success", false);
            map.put("msg", "用户名密码不能为空");
            return map;
        }
        return authService.register(user);
    }



}
