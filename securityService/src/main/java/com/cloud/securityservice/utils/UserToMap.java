package com.cloud.securityservice.utils;

import com.cloud.common.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class UserToMap {
    static public Map<String, Object> userToMap(User user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("last_login", user.getLast_login());
        map.put("count_size", user.getCount_size());
        map.put("total_size", user.getTotal_size());
        return map;
    }
}
