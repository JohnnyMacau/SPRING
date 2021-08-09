package com.macauslot.recruitmentadmin.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.macauslot.recruitmentadmin.exception.UserSkipValidation;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jim.deng
 */
public class DateTool {
    public static Date getDate(String year, String month, String day) throws ParseException {
        return DateUtils.parseDate(year + "-" + month + "-" + day, TimeEnum.YYYY_MM_DD.getName());
    }

    //LocalDate -> Date
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    //LocalDateTime -> Date
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    //Date -> LocalDate
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    //Date -> LocalDateTime
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

	public static String dateToString(Date date, String format) 
	{
		String result = "";
		if (date != null || format != null)
		{			
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			}
			catch(Exception e){
				e.printStackTrace();
			}	
		}
		return result;		
	}
	
	public static void checkDate(Date startDate, Date endDate) {
        Instant iStart = startDate.toInstant();
        Instant iEnd = endDate.toInstant();
        long l = Duration.between(iStart, iEnd).toDays();
        if (l < 1) {
            throw new UserSkipValidation("invalid date input");
        }
    }

	public static String getDateTimeString() {
        Calendar now = Calendar.getInstance();
        return new String(
                DateFormatUtils.format(now, "yyyy年MM月dd日(EEEE)-HH-mm-ss").getBytes(StandardCharsets.UTF_8)
                , StandardCharsets.ISO_8859_1);
    }
}
