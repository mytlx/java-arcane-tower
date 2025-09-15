package com.mytlx.handcraft.rpc.demo.common.service;

import com.mytlx.handcraft.rpc.model.RemoteService;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 0:15:29
 */
public interface BookingDetailService extends RemoteService {

    Object getBookingByUserId(Long userId);

    Object getBookingByUserId(Long userId, String userName);

    Object getBookingByUserId(String username);
}
