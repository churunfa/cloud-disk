package com.cloud.securityservice.utils;

import com.cloud.common.pojo.User;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserParse {
    static public Map<String, Object> userToMap(User user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("last_login", user.getLast_login());
        map.put("count_size", user.getCount_size());
        map.put("total_size", user.getTotal_size());
        return map;
    }
    static public User ClaimsToUser(Claims claims) {
        User user = new User();
        if (claims.get("id") != null) {
            String id = claims.get("id").toString();
            user.setId(Integer.parseInt(id));
        }
        if (claims.get("username") != null) {
            String username = claims.get("username").toString();
            user.setUsername(username);
        }
        if (claims.get("password") != null) {
            String password = claims.get("password").toString();
            user.setPassword(password);
        }
        if (claims.get("deleted") != null) {
            String deleted = claims.get("deleted").toString();
            user.setDeleted(Boolean.parseBoolean(deleted));
        }
        if (claims.get("locked") != null) {
            String locked = claims.get("locked").toString();
            user.setLocked(Boolean.parseBoolean(locked));
        }
        if (claims.get("gmt_create") != null) {
            String gmt_create = claims.get("gmt_create").toString();
            user.setGmt_create(new Date(Long.parseLong(gmt_create)));
        }
        if (claims.get("gmt_modified") != null) {
            String gmt_modified = claims.get("gmt_modified").toString();
            user.setGmt_modified(new Date(Long.parseLong(gmt_modified)));
        }
        if (claims.get("last_login") != null) {
            String last_login = claims.get("last_login").toString();
            user.setLast_login(new Date(Long.parseLong(last_login)));
        }
        if (claims.get("count_size") != null) {
            String count_size = claims.get("count_size").toString();
            user.setCount_size(Long.parseLong(count_size));
        }
        if (claims.get("total_size") != null) {
            String total_size = claims.get("total_size").toString();
            user.setTotal_size(Long.parseLong(total_size));
        }
        return user;
    }
}
