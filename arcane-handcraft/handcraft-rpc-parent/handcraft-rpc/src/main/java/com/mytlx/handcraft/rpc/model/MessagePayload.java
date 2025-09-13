package com.mytlx.handcraft.rpc.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 16:58:46
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MessagePayload implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * consumer id
     */
    private String clientId;

    @JSONField(serializeUsing = MessageTypeSerializer.class, deserializeUsing = MessageTypeDeserializer.class)
    private MessageType messageType;

    private Object payload;

    public MessagePayload of(String clientId, MessageType messageType, Object payload) {
        MessagePayload messagePayload = new MessagePayload()
                .setClientId(clientId)
                .setMessageType(messageType);

        // tlxTODO:
        if (messageType == MessageType.CALL) {
            messagePayload.setPayload(new MessagePayload.RpcRequest());
        } else if (messageType == MessageType.RESPONSE) {
            messagePayload.setPayload(new MessagePayload.RpcResponse());
        }
        return messagePayload;
    }

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class RpcRequest implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * producer id
         */
        private String requestClientId;

        private String requestId;

        private String requestMethodSimpleName;

        private String requestClassName;

        private String returnValueType;

        private String[] parameterTypes;

        private Object[] parameters;

    }

    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class RpcResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String requestId;

        private Object returnValue;

    }

}
