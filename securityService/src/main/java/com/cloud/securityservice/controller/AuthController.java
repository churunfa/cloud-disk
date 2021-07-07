package com.cloud.securityservice.controller;

import com.cloud.common.pojo.User;
import com.cloud.securityservice.service.UserService;
import com.cloud.securityservice.utils.UserToMap;
import org.churunfa.security.password.JWT.JWTUtils;
import org.churunfa.security.password.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class AuthController {
    JWTUtils jwtUtils;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserService userService;

    @Autowired
    public void setJwtUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setbCryptPasswordEncoder(org.churunfa.security.password.bcrypt.BCryptPasswordEncoder BCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = BCryptPasswordEncoder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map login(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user.getUsername() == null || user.getPassword() == null) {
            map.put("success", false);
            map.put("msg", "用户名或密码为空");
            return map;
        }

        User queryUser = userService.queryUserByUsername(user.getUsername());

        if (queryUser == null) {
            map.put("success", false);
            map.put("msg", "用户名或密码错误");
            return map;
        }

        boolean flag = bCryptPasswordEncoder.matches(user.getPassword(), queryUser.getPassword());

        if (!flag) {
            map.put("success", false);
            map.put("msg", "用户名或密码错误");
            return map;
        }

        if (queryUser.getLocked()) {
            map.put("success", false);
            map.put("msg", "用户被锁定");
            return map;
        }

        User updateUser = new User();
        updateUser.setId(queryUser.getId());
        updateUser.setLast_login(new Date());
        userService.update(updateUser);

        map.put("jwt_token", jwtUtils.createJWT(UserToMap.userToMap(queryUser)));
        map.put("success", true);
        return map;
    }

    @GetMapping(value = "/register")
    public Map register(User user) {
        Map<String, Object> map = new HashMap<>();
        int count = userService.insert(user);

        if (count == 0) map.put("success", false);
        else map.put("success", true);

        return map;
    }

}
