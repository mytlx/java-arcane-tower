package com.mytlx.arcane.handcraft.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 14:10:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HCTableName {

    String value();

}
