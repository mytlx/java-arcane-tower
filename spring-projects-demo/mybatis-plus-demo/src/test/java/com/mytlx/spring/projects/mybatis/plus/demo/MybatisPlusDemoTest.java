package com.mytlx.spring.projects.mybatis.plus.demo;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.mytlx.spring.projects.mybatis.plus.demo.entity.MybatisPlusDemoUser;
import com.mytlx.spring.projects.mybatis.plus.demo.mapper.MybatisPlusDemoUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 20:49
 */
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MybatisPlusDemoTest {

    @Autowired
    private MybatisPlusDemoUserMapper userMapper;
    
    @Test
    public void testInsert() throws Exception {
        MybatisPlusDemoUser user = new MybatisPlusDemoUser();
        user.setName("tlx");
        user.setAge(18);
        user.setEmail("tlx@qq.com");
        userMapper.insert(user);
        assertThat(user.getId()).isNotNull();
    }
    
}