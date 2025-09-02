package com.mytlx.arcane.handcraft.mybatis;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 13:58:29
 */
public interface UserMapper {

    User selectById(@HCParam("id") Long id);


}
