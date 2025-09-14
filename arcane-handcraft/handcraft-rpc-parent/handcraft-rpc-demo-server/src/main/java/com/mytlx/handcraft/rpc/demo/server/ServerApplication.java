package com.mytlx.handcraft.rpc.demo.server;

import com.mytlx.handcraft.rpc.config.EnableRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 15:40:05
 */
@SpringBootApplication
@EnableRpcServer
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
