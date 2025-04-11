package com.mytlx.spring.demo.factoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-08 9:13
 */
@Configuration
public class MyConfig {

    @Bean
    public MyFactoryBean myFactoryBean() {
        return new MyFactoryBean();
    }

}
