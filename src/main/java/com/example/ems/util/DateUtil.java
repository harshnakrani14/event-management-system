package com.example.ems.util;

import com.example.ems.dto.LocationDto;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static Date convertToUTC(LocalDateTime userDateTime) {
        return new LocalDateTime(userDateTime, DateTimeZone.forID("UTC")).toDate();
    }

    public static LocalDateTime convertToUserTimeZone(Date utcDateTime, String timeZone) {
        return new LocalDateTime(utcDateTime, DateTimeZone.forID(timeZone));
    }

    public static String getTimeZoneFromLocation(LocationDto locationDto) {
        TimeZone tz = TimeZone.getDefault(); // You can customize logic here
        return tz.getID(); // e.g., "Asia/Kolkata"
    }

}
