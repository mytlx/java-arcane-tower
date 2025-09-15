package com.mytlx.handcraft.rpc.demo.user.service;

import com.mytlx.handcraft.rpc.config.AutoRemoteInjection;
import com.mytlx.handcraft.rpc.demo.common.service.BookingDetailService;
import com.mytlx.handcraft.rpc.demo.common.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 16:07:11
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailService {

    // @Autowired
    // private RpcClient rpcClient;

    @AutoRemoteInjection(targetClientId = "demo-booking")
    private BookingDetailService bookingDetailService;

    @Override
    public String checkUser() {

        // BookingDetailService bookingDetailService = rpcClient.getProxy(BookingDetailService.class, "demo-booking");
        // Object result = bookingDetailService.getBookingByUserId(1L);

        String result = bookingDetailService.getBookingByUserId(1L);

        log.info("UserDetailServiceImpl#checkUser result: {}", result);

        return result;
    }

}
