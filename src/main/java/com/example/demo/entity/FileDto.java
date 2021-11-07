package com.example.demo.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(staticName = "empty")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class FileDto {

    String name;
    long size;
    @Value("${files}")
    String path;
    File file;

    public FileDto(String name, long size, String path){
        this.name = name;
        this.size = size;
        this.path = path;
    }

    public FileDto(String name, File file, String path){
        this.name = name;
        this.file = file;
        this.path = path;
    }

    public Path getPath(){
        return Paths.get(path);
    }
}
