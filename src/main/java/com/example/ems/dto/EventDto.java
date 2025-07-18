package com.example.ems.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

}