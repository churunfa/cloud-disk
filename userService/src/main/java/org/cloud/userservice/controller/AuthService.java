package org.cloud.userservice.controller;

import com.cloud.common.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "service-security")
public interface AuthService {
    @RequestMapping(value = "/user/login/", method = RequestMethod.POST)
    Map login(@RequestParam("username") String username, @RequestParam("password") String password);
    @GetMapping(value = "/user/register")
    Map register(@SpringQueryMap User user);

    @GetMapping(value = "/user/check")
    Map check(@RequestParam("jwt_token") String jwt_token);
}
