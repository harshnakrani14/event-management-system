package com.example.ems.model;

import com.example.ems.config.DateTimeValueConverter;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ValueConverter;

@Getter
@Setter
public abstract class BaseEntity {

    @Id
    private String id;

//    @CreatedDate
//    @Field("created_at")
    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime createdAt;

//    @LastModifiedDate
//    @Field("modified_at")
    @ValueConverter(DateTimeValueConverter.class)
    private LocalDateTime modifiedAt;


}



