package com.rest.hw.app.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePersonDTO {
    String name;
    Integer age;
    String email;

    public CreatePersonDTO() { }
}
