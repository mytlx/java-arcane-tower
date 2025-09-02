package com.mytlx.arcane.handcraft.schedule;

import com.mytlx.arcane.utils.date.LocalDateTimeUtils;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 22:23:12
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        HCScheduleService scheduleService = new HCScheduleService();
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTimeUtils.currentTime("HH:mm:ss SSS") + " 100ms/次");
        }, 100);

        Thread.sleep(1000);
        System.out.println("添加一个每200毫秒打印的定时任务");

        scheduleService.schedule(() -> {
            System.out.println(LocalDateTimeUtils.currentTime("HH:mm:ss SSS") + " 200ms/次");
        }, 200);
    }
}
