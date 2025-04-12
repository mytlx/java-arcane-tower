package com.mytlx.dev.solutions.drilldown.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 15:21
 */
@Data
@Accessors(chain = true)
public class User {

    private Long id;

    private String name;

    private Long shopId;

    private Long companyId;
    /**
     * 0: 登录用户，1：平台用户
     */
    private Integer platformFlag;

    /**
     *  1001：公司管理员
     *  1002：经纪人
     *  2001：平台管理员
     */
    private Integer roleType;

    private String cityIds;

    private Integer status;
}
