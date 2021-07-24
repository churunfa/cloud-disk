package org.cloud.uploadanddownload.controller;

import com.cloud.common.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "service-security")
public interface SecurityService {
    @GetMapping(value = "/user/verifyJwtToken")
    Map verifyJwtToken(@RequestParam("token") String token);

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    Map login(User user);

    @GetMapping(value = "/user/register")
    Map register(User user);

    @GetMapping(value = "/user/check")
    Map check(@RequestParam("jwt_token") String jwt_token);

    @RequestMapping("/user/getAllInfo")
    User getAllInfo(@RequestParam("jwt_token") String jwt_token);

}
