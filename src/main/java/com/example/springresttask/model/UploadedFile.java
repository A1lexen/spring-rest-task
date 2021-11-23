package com.example.springresttask.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
