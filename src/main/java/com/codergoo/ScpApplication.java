package com.codergoo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.codergoo.mapper")
public class ScpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScpApplication.class, args);
    }

}
