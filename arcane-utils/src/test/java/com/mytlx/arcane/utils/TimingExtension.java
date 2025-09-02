package com.mytlx.arcane.utils;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-13 16:41
 */
public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static final String START_TIME = "start time";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(START_TIME, System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        long startTime = context.getStore(ExtensionContext.Namespace.GLOBAL).remove(START_TIME, long.class);
        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("Test [%s] cost %d ms.%n", context.getDisplayName(), duration);
    }
}
