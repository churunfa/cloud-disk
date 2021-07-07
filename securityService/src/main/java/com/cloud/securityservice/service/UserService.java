package com.cloud.securityservice.service;

import com.cloud.common.pojo.User;

public interface UserService {
    User queryUserByUsername(String username);
    int insert(User user);
    int update(User user);
}
