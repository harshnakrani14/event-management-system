package com.example.ems.dto;

import com.example.ems.config.DateTimeValueConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.convert.ValueConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private String ticketId;
    private String eventId;
    private String userId;

    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime bookingTime;

    private int slotsRequested;

    private String timingId;

    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime createdAt;

    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime modifiedAt;

}
