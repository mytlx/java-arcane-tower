package com.mytlx.handcraft.rpc.demo.booking.service;

import com.mytlx.handcraft.rpc.model.RpcMethod;
import org.springframework.stereotype.Component;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 0:15:58
 */
@Component
public class BookingDetailServiceImpl implements BookingDetailService {

    @RpcMethod
    @Override
    public Object getBookingByUserId(Long userId) {
        return "i am booking service";
    }

    @Override
    public Object getBookingByUserId(Long userId, String userName) {
        return null;
    }

    @Override
    public Object getBookingByUserId(String username) {
        return null;
    }
}
