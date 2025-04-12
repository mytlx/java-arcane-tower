package com.mytlx.spring.projects.mybatis.plus.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 20:45
 */
@Data
@TableName("`mybatis_plus_demo_user`")
public class MybatisPlusDemoUser {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Integer age;

    private String email;
}
