package com.example.ems.dto;

import com.example.ems.config.DateTimeValueConverter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.convert.ValueConverter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String id;

    @NotBlank
    private String eventName;

    private LocationDto location;

    @NotNull
    private List<TimingDto> showTime;

    @NotBlank
    private String organizerUsername; // Instead of storing whole user

    @NotNull
    private int capacity;

    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime createdAt;

    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime modifiedAt;
}
