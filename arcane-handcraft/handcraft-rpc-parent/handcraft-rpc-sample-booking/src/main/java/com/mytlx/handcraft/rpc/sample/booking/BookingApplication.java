package com.mytlx.handcraft.rpc.sample.booking;

import com.mytlx.handcraft.rpc.config.EnableRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 23:47:38
 */
@SpringBootApplication
@EnableRpcClient(clientId = "sample-booking", packages = "com.mytlx.handcraft.rpc.sample.booking.service")
public class BookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
}
