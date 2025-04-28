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
public class TimingDto {
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double price;

    private int availableSlots;

}


