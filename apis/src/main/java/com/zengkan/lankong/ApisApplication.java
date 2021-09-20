package com.zengkan.lankong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@ComponentScan("com.zengkan")
@MapperScan("com.zengkan.lankong.mappers")
@SpringBootApplication
public class ApisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApisApplication.class, args);
    }

}
