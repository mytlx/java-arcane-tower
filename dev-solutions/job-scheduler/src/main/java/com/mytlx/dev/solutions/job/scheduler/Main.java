package com.mytlx.dev.solutions.job.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 14:32:53
 */
@SpringBootApplication
@MapperScan("com.mytlx.dev.solutions.job.scheduler.mapper")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
