package com.macauslot.recruitmentadmin.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: jim.deng
 * @Date: 2021/3/4 9:32
 */
public class DateBetweenUtil {
    public static List<String> getDateListBetween(String start, String end) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (Exception e) {
            throw new IllegalArgumentException("日期格式不正确。（日期示例：2019-12-26）");
        }
        return getDateStringList(startDate, endDate);
    }


    public static List<String> getDateListBetween(Date start, Date end) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = date2LocalDate(start);
            endDate = date2LocalDate(end);
        } catch (Exception e) {
            throw new IllegalArgumentException("日期格式不正确。date2LocalDate() error ");
        }
        return getDateStringList(startDate, endDate);
    }

    private static List<String> getDateStringList(LocalDate startDate, LocalDate endDate) {
        List<String> list = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(f.toString()));
        return list;
    }

    public static String convertDateToStr(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }
    public static Date getDateByCondition(Date now, int amount, int timeName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(timeName, amount);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     */
    public static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
