package com.cloud.securityservice.controller;

import com.cloud.common.pojo.User;
import com.cloud.securityservice.service.UserService;
import com.cloud.securityservice.utils.UserParse;
import io.jsonwebtoken.Claims;
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

        map.put("jwt_token", jwtUtils.createJWT(UserParse.userToMap(queryUser)));
        System.out.println(map.get("jwt_token"));
        map.put("success", true);

        User updateUser = new User();
        updateUser.setId(queryUser.getId());
        updateUser.setLast_login(new Date());
        userService.update(updateUser);
        map.put("user", UserParse.userToMap(queryUser));
        return map;
    }

    @GetMapping(value = "/register")
    public Map register(User user) {
        Map<String, Object> map = new HashMap<>();
        int count = userService.insert(user);

        if (count == 0) {
            map.put("success", false);
            map.put("msg", "用户名已存在");
        }
        else map.put("success", true);
        return map;
    }

    @GetMapping("/verifyJwtToken")
    public Map verifyJwtToken(String token){
        HashMap<String, Object> map = new HashMap<>();
        if (token == null) {
            map.put("success", false);
            map.put("msg", "用户未登陆");
        }
        try {
            Claims claims = jwtUtils.verifyJwt(token);
            map.put("success", true);
            map.put("user", UserParse.ClaimsToUser(claims));
        } catch (Exception e) {
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/check")
    public Map checkLogin(String jwt_token) {
        Map map = verifyJwtToken(jwt_token);

        if (!(Boolean)map.get("success")) return map;

        User user = (User) map.get("user");
        User queryUser = userService.queryUserById(user.getId());
        map.put("user", UserParse.userToMap(queryUser));
        return map;
    }

    @RequestMapping("/getAllInfo")
    public User getAllInfo(String jwt_token) {
        Map map = verifyJwtToken(jwt_token);
        if (!(Boolean)map.get("success")) return null;
        User user = (User) map.get("user");
        User queryUser = userService.queryUserById(user.getId());
        return queryUser;
    }

}
