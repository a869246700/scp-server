package com.codergoo;

import com.codergoo.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ScpApplicationTests {

    @Autowired
    public RedisUtil redisUtil;
    
    @Test
    void contextLoads() {
    }
    
    @Test
    void testRedis() {
        redisUtil.set("username", "coderGoo");
        System.out.println(redisUtil.get("username"));
    }
}
