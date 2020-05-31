package org.treeleafj.xmax.test;

import org.apache.catalina.startup.UserConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treeleafj.xmax.boot.EnableXMax;

import java.util.concurrent.TimeUnit;

@RestController
@EnableXMax
@SpringBootApplication
public class TestApplication {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("login")
    public User login(User user) {

        User u = null;
        if (StringUtils.isNotBlank(user.getUsername())) {
            u = (User) redisTemplate.opsForValue().get(user.getUsername());
            if (u == null) {
                u = user;
                u.setPassword("123");
                redisTemplate.opsForValue().set(u.getUsername(), u, 100, TimeUnit.SECONDS);
            }
        }

        return u;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
