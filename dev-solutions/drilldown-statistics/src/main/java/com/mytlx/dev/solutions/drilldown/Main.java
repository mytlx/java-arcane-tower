package com.mytlx.dev.solutions.drilldown;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:01
 */
@SpringBootApplication
@MapperScan("com.mytlx.dev.solutions.drilldown.mapper")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
