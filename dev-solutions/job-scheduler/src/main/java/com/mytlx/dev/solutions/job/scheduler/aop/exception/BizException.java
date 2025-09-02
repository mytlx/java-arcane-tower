package com.mytlx.dev.solutions.job.scheduler.aop.exception;

import lombok.Getter;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 20:27:41
 */
@Getter
public class BizException extends RuntimeException {
    private final int code;

    public BizException() {
        super("系统异常");
        this.code = -100;
    }

    public BizException(String msg) {
        super(msg);
        this.code = -100;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

}
