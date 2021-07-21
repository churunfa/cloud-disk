package org.churunfa.security.grant.auth.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/error")
public class PermissionDenied {
    @RequestMapping("/forbidden")
    public ResponseEntity<Map> Forbidden() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("msg", "Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }

    @RequestMapping("/unauthorized")
    public ResponseEntity<Map> Unauthorized() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("msg", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}
