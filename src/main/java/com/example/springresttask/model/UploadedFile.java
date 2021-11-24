package com.example.springresttask.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadedFile {

    int fileId;
    String fileName;
    String fileType;
    byte[] fileData;
    String path;

}
