package com.mytlx.arcane.handcraft.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 13:40:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@HCTableName("t_handcraft_mybatis_user")
public class User {

    private Long id;

    private String name;

    private Integer age;

}
