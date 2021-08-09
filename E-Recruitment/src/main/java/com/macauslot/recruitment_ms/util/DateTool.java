package com.macauslot.recruitment_ms.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author jim.deng
 */
public class DateTool {
    public static Date getDate(String year, String month, String day) throws ParseException {
        return DateUtils.parseDate(year + "-" + month + "-" + day, TimeEnum.YYYY_MM_DD.getName());
    }



}
