package com.example.ems.model;

import com.example.ems.model.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tickets")
public class Ticket extends BaseEntity {

    @DocumentReference(collection = "events")
    private Event event;

    @DocumentReference(collection = "users")
    private User user;

    private Date bookingTime; // Automatically set on creation

    private int slotsBooked;
    private String timingId;

}