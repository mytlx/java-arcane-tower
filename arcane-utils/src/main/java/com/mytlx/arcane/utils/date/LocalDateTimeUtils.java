package com.mytlx.arcane.utils.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * LocalDateTime 工具类
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-10 23:10
 */
public class LocalDateTimeUtils {

    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

    /**
     * Date 转 LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     */
    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间戳（毫秒）
     */
    public static long getCurrentTimestampMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前时间戳（秒）
     */
    public static long getCurrentTimestampSeconds() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 获取当前日期时间（默认格式）
     */
    public static String now() {
        return LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String now(String formatter) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
    }

    public static String now(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 获取当前日期（默认格式）
     */
    public static String today() {
        return LocalDate.now().format(DEFAULT_DATE_FORMATTER);
    }

    public static String today(String formatter) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(formatter));
    }

    public static String today(DateTimeFormatter formatter) {
        return LocalDate.now().format(formatter);
    }

    /**
     * 获取当前时间（默认格式）
     */
    public static String currentTime() {
        return LocalTime.now().format(DEFAULT_TIME_FORMATTER);
    }

    public static String currentTime(String formatter) {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(formatter));
    }

    public static String currentTime(DateTimeFormatter formatter) {
        return LocalTime.now().format(formatter);
    }

    /**
     * 字符串转 LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTimeStr, String formatter) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(formatter));
    }

    public static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /**
     * 字符串转 LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER);
    }

    public static LocalDate parseDate(String dateStr, String formatter) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(formatter));
    }

    public static LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * LocalDateTime 转字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime, String formatter) {
        return dateTime.format(DateTimeFormatter.ofPattern(formatter));
    }

    public static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    public static String formatDateTime(Date date) {
        return formatDateTime(dateToLocalDateTime(date));
    }

    public static String formatDateTime(Date date, String formatter) {
        return formatDateTime(dateToLocalDateTime(date), formatter);
    }

    public static String formatDateTime(Date date, DateTimeFormatter formatter) {
        return formatDateTime(dateToLocalDateTime(date), formatter);
    }

    /**
     * LocalDate 转字符串
     */
    public static String formatDate(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }

    public static String formatDate(LocalDate date, String formatter) {
        return date.format(DateTimeFormatter.ofPattern(formatter));
    }

    public static String formatDate(LocalDate date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }

    public static String formatDate(Date date) {
        return formatDate(dateToLocalDateTime(date).toLocalDate());
    }

    public static String formatDate(Date date, String formatter) {
        return formatDate(dateToLocalDateTime(date).toLocalDate(), formatter);
    }

    public static String formatDate(Date date, DateTimeFormatter formatter) {
        return formatDate(dateToLocalDateTime(date).toLocalDate(), formatter);
    }


    /**
     * 时间戳（毫秒）转 LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 时间戳（毫秒）
     */
    public static long localDateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 日期加天数
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    /**
     * 日期减天数
     */
    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime.minusDays(days);
    }

    /**
     * 计算两个日期之间的天数
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 获取某天的开始时间（00:00:00）
     */
    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取某天的结束时间（23:59:59.999）
     */
    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取当前是星期几
     */
    public static String getWeekDayName(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
    }

    public static String getWeekDayName(LocalDateTime dateTime, TextStyle style) {
        return dateTime.getDayOfWeek().getDisplayName(style, Locale.getDefault());
    }

    public static int getWeekDay(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek().getValue();
    }
}
