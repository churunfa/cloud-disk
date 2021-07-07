package org.cloud.userservice.controller;

import com.cloud.common.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "service-security")
public interface AutoService {
    @RequestMapping(value = "/user/login/", method = RequestMethod.POST)
    Map login(@RequestParam("username") String username, @RequestParam("password") String password);
    @GetMapping(value = "/user/register")
    Map register(@SpringQueryMap User user);
}
