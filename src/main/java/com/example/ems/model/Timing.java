package com.example.ems.model;

import com.example.ems.config.DateTimeValueConverter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Timing {

    @Field("timingId")
    private String timingId = UUID.randomUUID().toString().replace("-","");

    @NotBlank
    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime startTime;

    @NotBlank
    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime endTime;

    private double price;

    private int availableSlots;

}
