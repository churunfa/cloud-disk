package org.cloud.userservice.controller;

import org.churunfa.security.grant.auth.Login;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class LoginTestController {

    @RequestMapping("/info")
    @Login
    public Object LoginStatusTest() {
        return "用户已登陆";
    }
}
