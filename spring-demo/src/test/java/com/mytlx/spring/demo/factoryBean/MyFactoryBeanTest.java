package com.mytlx.spring.demo.factoryBean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-08 9:56
 */
@SpringBootTest
class MyFactoryBeanTest {

    @Autowired
    private MyService myService;

    @Autowired
    @Qualifier("&myFactoryBean")
    private MyFactoryBean myFactoryBean;

    @Test
    public void test01() throws Exception {
        System.out.println("myService = " + myService);
        System.out.println("myFactoryBean = " + myFactoryBean);
        System.out.println("myFactoryBean.getObject = " + myFactoryBean.getObject());
    }
}