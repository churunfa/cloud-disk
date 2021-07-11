package com.cloud.securityservice.service;

import com.cloud.common.pojo.User;
import com.cloud.securityservice.mapper.UserMapper;
import org.churunfa.security.password.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User queryUserByUsername(String username) {
        return userMapper.queryUserByUsername(username);
    }

    @Override
    public User queryUserById(int id) {
        return userMapper.queryUserById(id);
    }

    @Override
    public int insert(User user) {
        if (user.getGmt_create() == null) user.setGmt_create(new Date());
        if (user.getGmt_modified() == null) user.setGmt_modified(new Date());
        if (user.getLast_login() == null) user.setLast_login(new Date());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println(user);

        int tot = 0;
        try {
            tot = userMapper.insertUser(user);
        } catch (Exception e){
//            e.printStackTrace();
        }
        return tot;
    }

    @Override
    public int update(User user) {
        if (user.getPassword() != null)
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getId() == null) return 0;
        return userMapper.updateUser(user);
    }
}
