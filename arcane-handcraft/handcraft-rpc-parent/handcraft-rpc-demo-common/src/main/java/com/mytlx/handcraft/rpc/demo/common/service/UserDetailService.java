package com.mytlx.handcraft.rpc.demo.common.service;

import com.mytlx.handcraft.rpc.model.RemoteService;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 16:06:50
 */
public interface UserDetailService extends RemoteService {
    String checkUser();
}
