package com.mytlx.handcraft.rpc.demo.user.rpc;

import com.mytlx.handcraft.rpc.config.AutoRemoteInjection;
import com.mytlx.handcraft.rpc.demo.common.service.BookingDetailService;
import com.mytlx.handcraft.rpc.demo.user.service.BookingServiceFallback;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 21:23:31
 */
@AutoRemoteInjection(targetClientId = "demo-booking", fallbackClass = BookingServiceFallback.class)
public interface UserBookingRpcService extends BookingDetailService {
}
