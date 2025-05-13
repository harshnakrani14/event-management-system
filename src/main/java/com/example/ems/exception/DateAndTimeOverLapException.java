package com.example.ems.exception;

import com.example.ems.model.core.Location;

import java.util.Date;


public class DateAndTimeOverLapException extends RuntimeException {

    public DateAndTimeOverLapException(Date startTime1, Date endTime1,
                                       Date startTime2, Date endTime2) {
        super(startTime1 + " - " + endTime1 + " overlaps with " + startTime2 + " - " + endTime2);
    }

    public DateAndTimeOverLapException(Date startTime1, Date endTime1,
                                       Date startTime2, Date endTime2, Location location) {
        super(startTime1 + " - " + endTime1 + " overlaps with " + startTime2 + " - "
                + endTime2 + " at venue " + location);
    }

    public DateAndTimeOverLapException(String msg) {
        super(msg);
    }

}