package com.example.springresttask.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadedFile {

    int fileId;
    String fileName;
    String fileType;
    byte[] fileData;
    String path;

    public UploadedFile(int id, MultipartFile file) {
        fileId = id;
        fileName = file.getOriginalFilename();
        int lastIndex = fileName.lastIndexOf('.');
        fileType = fileName.substring(lastIndex, fileName.length());
        try {
            fileData = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = System.getProperty("user.dir") +
                "\\src\\main\\resources\\uploadFiles\\" +
                fileName;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
