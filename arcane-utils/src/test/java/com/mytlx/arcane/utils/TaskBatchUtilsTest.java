package com.mytlx.arcane.utils;

import com.mytlx.arcane.utils.json.gson.GsonUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-13 16:30
 */
@ExtendWith(TimingExtension.class)
class TaskBatchUtilsTest {

    static List<String> taskList = new ArrayList<>(100);
    static ExecutorService ex = Executors.newFixedThreadPool(10);


    @BeforeAll
    public static void init() {
        for (int i = 0; i < 100; i++) {
            taskList.add("task--" + i);
        }
    }

    @SneakyThrows
    public static void mockHandle(String task) {
        Thread.sleep(1000);
        System.out.println("任务处理完成：" + task);
    }

    public static String mockWithResult(String task) {
        String res = "任务处理完成：" + task;
        System.out.println(res);
        return res;
    }

    @Test
    public void testSerial() throws Exception {
        for (String s : taskList) {
            mockHandle(s);
        }
    }

    @Test
    void disposeLatch() throws Exception {
        TaskBatchUtils.disposeLatch(taskList, TaskBatchUtilsTest::mockHandle, ex);
    }

    @Test
    void dispose() throws Exception {
        TaskBatchUtils.dispose(taskList, TaskBatchUtilsTest::mockHandle, ex);
    }

    @Test
    void testDispose() throws Exception {
        TaskBatchUtils.dispose(taskList, TaskBatchUtilsTest::mockHandle, ex);
    }

    @Test
    void executeWithResult() throws Exception {
        List<String> result = TaskBatchUtils.executeWithResult(taskList, TaskBatchUtilsTest::mockWithResult, ex);
        System.out.println("result = " + GsonUtils.toJson(result));
    }
}