package com.example.ems.dto;

import com.example.ems.config.DateTimeValueConverter;
import com.example.ems.util.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.convert.ValueConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //it's use for set permission
    private String password;

    private LocationDto location;

   @NotNull
    private Role role;

   @ValueConverter(DateTimeValueConverter.class)
   private LocalDateTime createdAt;

   @ValueConverter(DateTimeValueConverter.class)
   private LocalDateTime modifiedAt;
}

