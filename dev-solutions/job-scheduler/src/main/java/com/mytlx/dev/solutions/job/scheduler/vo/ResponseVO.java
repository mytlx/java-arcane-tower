package com.mytlx.dev.solutions.job.scheduler.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:49:17
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseVO<T> {

    public static final int FAIL_CODE = -100;

    private int code;

    private String msg;

    private T data;

    public static <T> ResponseVO<T> of(int code, String msg, T data) {
        return new ResponseVO<>(code, msg, data);
    }

    public static ResponseVO<String> success() {
        return success(null,"ok");
    }

    public static ResponseVO<String> success(String msg) {
        return success(null, msg);
    }

    public static <T> ResponseVO<T> success(T data) {
        return success(data, "ok");
    }

    public static <T> ResponseVO<T> success(T data, String msg) {
        return of(200, msg, data);
    }

    public static <T> ResponseVO<T> error(int code) {
        return error(code, "error");
    }

    public static <T> ResponseVO<T> error(String msg) {
        return error(FAIL_CODE, msg);
    }

    public static <T> ResponseVO<T> error(int code, String msg) {
        return of(code, msg, null);
    }
}
