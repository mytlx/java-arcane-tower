package com.mytlx.handcraft.rpc.demo.user;

import com.mytlx.handcraft.rpc.config.EnableRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 15:43:22
 */
@SpringBootApplication
@EnableRpcClient(clientId = "demo-user", packages = "com.mytlx.handcraft.rpc.demo.user.service")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
