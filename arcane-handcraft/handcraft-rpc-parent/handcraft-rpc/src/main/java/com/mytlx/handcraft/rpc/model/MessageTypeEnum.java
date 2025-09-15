package com.mytlx.handcraft.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 16:59:24
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    REGISTER(1),
    CALL(2),
    FORWARD(3),
    RESPONSE(4),

    HEARTBEAT(10),

    UNKNOWN(-1);

    ;

    private final int code;

    public static MessageTypeEnum getByCode(int code) {
        for (MessageTypeEnum type : MessageTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
