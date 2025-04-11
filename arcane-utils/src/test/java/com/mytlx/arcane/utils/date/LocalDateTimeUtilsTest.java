package com.mytlx.arcane.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-10 23:13
 */
public class LocalDateTimeUtilsTest {

    public static void main(String[] args) {
        System.out.println("当前时间：" + LocalDateTimeUtils.now());
        System.out.println("当前时间：" + LocalDateTimeUtils.now("yyyy年MM月dd日 HH时mm分ss秒"));
        System.out.println("当前时间：" + LocalDateTimeUtils.now(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("当前日期：" + LocalDateTimeUtils.today());
        System.out.println("当前日期：" + LocalDateTimeUtils.today("yyyy年MM月dd日"));
        System.out.println("当前日期：" + LocalDateTimeUtils.today(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("当前时间（时分秒）：" + LocalDateTimeUtils.currentTime());
        System.out.println("当前时间（时分秒）：" + LocalDateTimeUtils.currentTime("HH时mm分ss秒"));
        System.out.println("当前时间（时分秒）：" + LocalDateTimeUtils.currentTime(DateTimeFormatter.ISO_LOCAL_TIME));

        String dateStr = "2024-04-03 14:30:00";
        LocalDateTime dateTime = LocalDateTimeUtils.parseDateTime(dateStr);
        System.out.println("解析后的 LocalDateTime：" + dateTime);

        long timestamp = LocalDateTimeUtils.localDateTimeToTimestamp(dateTime);
        System.out.println("转换成时间戳：" + timestamp);

        LocalDateTime fromTimestamp = LocalDateTimeUtils.timestampToLocalDateTime(timestamp);
        System.out.println("时间戳转回 LocalDateTime：" + fromTimestamp);

        System.out.println("增加 3 天：" + LocalDateTimeUtils.plusDays(dateTime, 3));
        System.out.println("增加 -3 天：" + LocalDateTimeUtils.plusDays(dateTime, -3));
        System.out.println("减少 3 天：" + LocalDateTimeUtils.minusDays(dateTime, 3));
        System.out.println("减少 -3 天：" + LocalDateTimeUtils.minusDays(dateTime, -3));

        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 10);
        System.out.println("日期间隔天数：" + LocalDateTimeUtils.daysBetween(startDate, endDate));

        System.out.println("某天开始时间：" + LocalDateTimeUtils.getStartOfDay(dateTime));
        System.out.println("某天结束时间：" + LocalDateTimeUtils.getEndOfDay(dateTime));

        System.out.println("星期几：" + LocalDateTimeUtils.getWeekDayName(dateTime));
        System.out.println("星期几：" + LocalDateTimeUtils.getWeekDayName(dateTime, TextStyle.NARROW));
        System.out.println("星期几：" + LocalDateTimeUtils.getWeekDayName(dateTime, TextStyle.SHORT));
        System.out.println("星期几：" + LocalDateTimeUtils.getWeekDay(dateTime));

        Date date = new Date();
        System.out.println("Date 转 LocalDateTime：" + LocalDateTimeUtils.dateToLocalDateTime(date));
        System.out.println("LocalDateTime 转 Date：" + LocalDateTimeUtils.localDateTimeToDate(LocalDateTime.now()));

    }
    
}