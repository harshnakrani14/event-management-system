package com.example.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private String ticketId;
    private String eventId;
    private String userId;
    private LocalDateTime bookingTime;
    private int slotsRequested;
    private String timingId;

}