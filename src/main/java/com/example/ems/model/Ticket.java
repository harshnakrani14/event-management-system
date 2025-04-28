package com.example.ems.model;

import com.example.ems.config.DateTimeValueConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tickets")
public class Ticket extends BaseEntity{

    @DocumentReference(collection = "events")
    private Event event;

    @DocumentReference(collection = "users")
    private User user;

    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime bookingTime; // Automatically set on creation

    private int slotsBooked;

    private String timingId;

}

