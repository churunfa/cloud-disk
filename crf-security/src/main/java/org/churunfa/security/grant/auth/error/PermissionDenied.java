package org.churunfa.security.grant.auth.error;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/error")
public class PermissionDenied {
    @RequestMapping("/PermissionDenied")
    public Map PermissionDenied() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("msg", "Permission Denied");
        return map;
    }
}
