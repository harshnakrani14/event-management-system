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
public class TicketResponseDto {

    private String eventName;
    private LocationDto location;
    private TimingDto timing;
    private String name;
    private LocalDateTime bookingTime;
    private int slotsBooked;

}