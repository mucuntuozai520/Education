package com.linfeng.educenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Yu1
 * @date 2022/9/26 - 23:23
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.linfeng"})
@MapperScan("com.linfeng.educenter.mapper")
public class UcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }
}
