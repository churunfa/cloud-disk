package org.cloud.userservice.controller;

import com.cloud.common.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private AutoService autoService;

    @Autowired
    public void setAutoService(AutoService autoService) {
        this.autoService = autoService;
    }

    @PostMapping("/login")
    private Map login(String username, String password) {
        return autoService.login(username, password);
    }

    @PostMapping("/register")
    private Map register(User user) {
        HashMap<String, Object> map = new HashMap<>();
        if (user.getUsername() == null || user.getPassword() == null) {
            map.put("success", false);
            map.put("msg", "用户名密码不能为空");
            return map;
        }
        return autoService.register(user);
    }
}
