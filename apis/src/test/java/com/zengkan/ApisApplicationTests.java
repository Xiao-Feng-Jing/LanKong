package com.zengkan;

import com.zengkan.lankong.ApisApplication;
import com.zengkan.lankong.service.CategoryService;
import com.zengkan.lankong.utils.JwtUtil;
import com.zengkan.lankong.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = ApisApplication.class)
class ApisApplicationTests {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        System.out.println(redisUtil.zScore("my",1));
    }

}
