package com.mytlx.handcraft.rpc.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 23:43:33
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RpcClientRegistrar.class, RpcServiceScanPostProcessor.class})
public @interface EnableRpcClient {

    String clientId() default "";

    String[] packages() default {};
}
