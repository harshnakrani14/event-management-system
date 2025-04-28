package com.example.ems.config;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.data.convert.ValueConversionContext;
import org.springframework.data.mapping.PersistentProperty;

public class DateTimeValueConverter implements PropertyValueConverter<LocalDateTime, String, ValueConversionContext<? extends PersistentProperty<?>>>{

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime read(String value, ValueConversionContext<? extends PersistentProperty<?>> context) {
        if (value.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value, DATETIME_FORMATTER);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-dd'T'HH:mm:ss", e);
        }
    }

    @Override
    public String write(LocalDateTime value, ValueConversionContext<? extends PersistentProperty<?>> context) {
        return  value.toString(DATETIME_FORMATTER);
    }
}

