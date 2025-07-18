package com.example.ems.util;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.Date;

public class DateUtil {

    public static Date convertToUTC(LocalDateTime userDateTime) {
        return new LocalDateTime(userDateTime, DateTimeZone.forID("UTC")).toDate();
    }

    public static LocalDateTime convertToUserTimeZone(Date utcDateTime, String timeZone) {
        return new LocalDateTime(utcDateTime, DateTimeZone.forID(timeZone));
    }

}
