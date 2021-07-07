package org.cloud.userservice;

import org.churunfa.security.password.bcrypt.BCryptPasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void redisTestTemplate() {
        redisTemplate.opsForValue().set("name", "abcd");
//        String name = (String) redisTemplate.opsForValue().get("name1");
//        System.out.println(name);
//        redisTemplate.delete("name1");
    }

}
