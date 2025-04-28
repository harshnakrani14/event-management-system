package com.example.ems.util.exception;

import com.example.ems.model.Location;
import org.joda.time.LocalDateTime;


public class DateAndTimeOverLapException extends RuntimeException {

    public DateAndTimeOverLapException(LocalDateTime startTime1, LocalDateTime endTime1,
                                       LocalDateTime startTime2, LocalDateTime endTime2) {
        super(startTime1 + " - " + endTime1 + " overlaps with " + startTime2 + " - " + endTime2);
    }

    public DateAndTimeOverLapException(LocalDateTime startTime1, LocalDateTime endTime1,
                                       LocalDateTime startTime2, LocalDateTime endTime2, Location location) {
        super(startTime1 + " - " + endTime1 + " overlaps with " + startTime2 + " - " + endTime2 + " at venue " + location);
    }

    public DateAndTimeOverLapException(String msg) {
        super(msg);
    }
}
