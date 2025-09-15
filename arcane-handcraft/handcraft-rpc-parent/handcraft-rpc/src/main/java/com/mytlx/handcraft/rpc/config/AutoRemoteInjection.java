package com.mytlx.handcraft.rpc.config;

import java.lang.annotation.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-15 14:57:45
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoRemoteInjection {

    String targetClientId() default "";

}
