package com.mytlx.arcane.study.netty.practice.chat.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 16:08:46
 */
@Data
public class ClientConfig {

    private List<ClientInfo> clients;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClientInfo {
        private String username;
        private String password;
    }
}
