package com.mytlx.handcraft.rpc.server;

import com.mytlx.handcraft.rpc.model.MessagePayload;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 20:19:01
 */
@Slf4j
public class ClientSessionManager {

    // clientId -> channel
    private static final Map<String, Channel> CLIENT_ID_CHANNEL_MAP = new ConcurrentHashMap<>();

    // requestId -> messagePayload
    private static final Map<String, MessagePayload> REQUEST_ID_MSG_MAP = new ConcurrentHashMap<>();

    public static void register(final String clientId, final Channel channel) {
        CLIENT_ID_CHANNEL_MAP.put(clientId, channel);
    }

    public static boolean isRegistered(final String clientId) {
        return CLIENT_ID_CHANNEL_MAP.containsKey(clientId);
    }

    public static Channel getChannel(final String clientId) {
        return CLIENT_ID_CHANNEL_MAP.get(clientId);
    }

    public static void signOut(final String clientId) {
        CLIENT_ID_CHANNEL_MAP.remove(clientId);
    }

    public static void putRequest(MessagePayload msg) {
        MessagePayload.RpcRequest request = (MessagePayload.RpcRequest) msg.getPayload();
        REQUEST_ID_MSG_MAP.put(request.getRequestId(), msg);
    }

    public static MessagePayload getRequest(final String requestId) {
        return REQUEST_ID_MSG_MAP.get(requestId);
    }

    public static void deleteRequest(final String requestId) {
        REQUEST_ID_MSG_MAP.remove(requestId);
    }
}
