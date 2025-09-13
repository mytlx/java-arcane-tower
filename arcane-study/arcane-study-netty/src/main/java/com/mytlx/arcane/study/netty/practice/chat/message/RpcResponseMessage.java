package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

/**
 * @author yihang
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
