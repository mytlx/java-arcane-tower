package com.mytlx.spring.projects.mybatis.plus.demo.service;

import com.mytlx.spring.projects.mybatis.plus.demo.entity.MybatisPlusDemoUser;
import com.mytlx.spring.projects.mybatis.plus.demo.mapper.MybatisPlusDemoUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 21:00
 */
@Slf4j
@Service
public class DemoService {

    @Autowired
    private MybatisPlusDemoUserMapper userMapper;

    public void testInsertAndSelect() {
        MybatisPlusDemoUser user = new MybatisPlusDemoUser();
        user.setName("mytlx");
        user.setAge(28);
        user.setEmail("mytlx@example.com");

        userMapper.insert(user);
        log.info("插入后的ID：{}", user.getId());

        MybatisPlusDemoUser dbUser = userMapper.selectById(user.getId());
        log.info("数据库中的用户：{}", dbUser);
    }


}
