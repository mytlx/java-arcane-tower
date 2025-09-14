package com.mytlx.handcraft.rpc.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 15:33:08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RpcServerConfiguration.class)
public @interface EnableRpcServer {
}
