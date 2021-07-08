package org.churunfa.security.autoConfigaration;

import com.cloud.common.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//@FeignClient(value = "service-security")
public interface SecurityService {
    @GetMapping(value = "/user/verifyJwtToken")
    Map verifyJwtToken(@RequestParam("token") String token);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    Map login(User user);

    @GetMapping(value = "/register")
    Map register(User user);
}
