package com.example.ems.model.core;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Timing {

    @Field("timingId")
    private String timingId = UUID.randomUUID().toString().replace("-","");

    @NotBlank
    private Date startTime;

    @NotBlank
    private Date endTime;

    private double price;
    private int availableSlots;

}