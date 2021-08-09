package com.macauslot.recruitmentadmin.util;


/**
 * @Description:返回 LocalDateTime 格式的日、周、月、季度、年起止时间
 * @Author: lsiyan
 * @Date: 2020/8/21 22:47
 */

import java.time.*;

/**
 * jdk8 获取当天，本周，本月，本季度，本年起始时间工具类 LocalDateTime
 */
public class LocalDateTimeUtils {

    public static final String MinTime = "T00:00:00";
    public static final String MaxTime = "T23:59:59.999999999";


    /**
     * @Description:当天的开始时间
     * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
     */
    public static LocalDateTime getStartOrEndDayOfDay(LocalDate today, Boolean isFirst) {
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        if (isFirst) {
            return LocalDateTime.of(today, LocalTime.MIN);
        } else {
            return LocalDateTime.of(today, LocalTime.MAX);
        }
    }

    /**
     * @Description:本周的开始时间
     * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
     */
    public static LocalDateTime getStartOrEndDayOfWeek(LocalDate today, Boolean isFirst) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        DayOfWeek week = today.getDayOfWeek();
        int value = week.getValue();
        if (isFirst) {
            resDate = today.minusDays(value - 1);
        } else {
            resDate = today.plusDays(7 - value);
            time = MaxTime;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(resDate.toString() + time);
        return localDateTime;
    }

    /**
     * @Description:本月的开始时间
     * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
     */
    public static LocalDateTime getStartOrEndDayOfMonth(LocalDate today, Boolean isFirst) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        int length = month.length(today.isLeapYear());
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), month, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), month, length);
            time = MinTime;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(resDate.toString() + time);
        return localDateTime;
    }

    /**
     * @Description:本季度的开始时间
     * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
     */
    public static LocalDateTime getStartOrEndDayOfQuarter(LocalDate today, Boolean isFirst) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
            time = MaxTime;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(resDate.toString() + time);
        return localDateTime;
    }

    /**
     * @Description:本年度的开始时间
     * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
     */
    public static LocalDateTime getStartOrEndDayOfYear(LocalDate today, Boolean isFirst) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), Month.JANUARY, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), Month.DECEMBER, Month.DECEMBER.length(today.isLeapYear()));
            time = MaxTime;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(resDate.toString() + time);
        return localDateTime;
    }
}
