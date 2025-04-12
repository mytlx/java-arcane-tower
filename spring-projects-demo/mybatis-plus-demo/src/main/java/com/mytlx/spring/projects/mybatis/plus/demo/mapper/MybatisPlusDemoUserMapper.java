package com.mytlx.spring.projects.mybatis.plus.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytlx.spring.projects.mybatis.plus.demo.entity.MybatisPlusDemoUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 20:47
 */
@Mapper
public interface MybatisPlusDemoUserMapper extends BaseMapper<MybatisPlusDemoUser> {

}
