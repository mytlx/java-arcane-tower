package com.mytlx.spring.projects.mybatis.plus.demo;

import com.mytlx.spring.projects.mybatis.plus.demo.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 21:02
 */
@SpringBootTest
public class DemoTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void testInsertAndGet() throws Exception {
        demoService.testInsertAndSelect();
    }


}
