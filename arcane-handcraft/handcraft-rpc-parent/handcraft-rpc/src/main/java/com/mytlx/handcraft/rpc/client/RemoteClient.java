package com.mytlx.handcraft.rpc.client;

import com.mytlx.handcraft.rpc.model.MessagePayload;

import java.util.concurrent.CompletableFuture;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 13:56:25
 */
public interface RemoteClient {

    void doRequest(MessagePayload msg, String requestId, CompletableFuture<MessagePayload.RpcResponse> future);

    void sendMessage(MessagePayload msg);

    void didCatchResponse(MessagePayload msg, String requestId);

    String getClientId();

}
