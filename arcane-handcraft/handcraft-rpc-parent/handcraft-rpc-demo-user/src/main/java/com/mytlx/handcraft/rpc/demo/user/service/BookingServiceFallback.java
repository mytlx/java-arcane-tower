package com.mytlx.handcraft.rpc.demo.user.service;

import com.mytlx.arcane.utils.date.LocalDateTimeUtils;
import com.mytlx.handcraft.rpc.demo.common.service.BookingDetailService;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 14:44:54
 */
public class BookingServiceFallback implements BookingDetailService {
    @Override
    public String getBookingByUserId(Long userId) {
        return "fallback " + LocalDateTimeUtils.now();
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
