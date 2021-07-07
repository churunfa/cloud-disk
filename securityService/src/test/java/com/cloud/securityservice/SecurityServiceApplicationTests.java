package com.cloud.securityservice;

import com.cloud.common.pojo.User;
import com.cloud.securityservice.mapper.UserMapper;
import com.cloud.securityservice.service.UserService;
import org.churunfa.security.password.bcrypt.BCryptPasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityServiceApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void mybatisTest() {
        User admin1 = userMapper.queryUserByUsername("admin");
        System.out.println(admin1);
        User admin2 = userMapper.queryUserByUsername("admin");
        System.out.println(admin2);
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void redisTestTemplate() {
        redisTemplate.opsForValue().set("name", "abc");
//        String name = (String) redisTemplate.opsForValue().get("name1");
//        System.out.println(name);
//        redisTemplate.delete("name1");
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void BCryptPasswordEncoder() {
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Autowired
    UserService userService;

    @Test
    public void insertTest() {
        User user = new User();
        user.setUsername("crf");
        user.setPassword("123456");
        int count = userService.insert(user);
        System.out.println(count);
    }

    @Test
    public void update() {
        User user = new User();
        user.setId(1);
        user.setUsername("root");
        int count = userService.update(user);
        System.out.println(count);
    }

}
