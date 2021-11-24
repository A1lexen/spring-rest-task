package com.example.resttask;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileService {

    @Value("${path}")
    String uploadPath;

    public void uploadFile(String name, MultipartFile multipartFile) {
        File file = new File(uploadPath + name);
        System.out.println("file path: " + uploadPath);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStreamResource downloadFile(String name) throws FileNotFoundException {
        return new InputStreamResource(new FileInputStream(new File(uploadPath + name)));
    }

    public List<FileDto> listFiles() {
        List<FileDto> filesDto = new ArrayList<>();
        List<File>  files = List.of(new File(uploadPath).listFiles());
        for (File file : files) {
            filesDto.add(new FileDto(file.getName(), file.length(), file.getPath()));
        }
        return filesDto;
    }

    public void updateFile(String name, String newName) {
        File file = new File(uploadPath + name);
        file.renameTo(new File(uploadPath + newName));
    }

    public void deleteFile(String name) {
        File file = new File(uploadPath + name);
        file.delete();
    }

    public FileDto getFile(String name) {
        File file = new File(uploadPath + name);
        FileDto fileDto = new FileDto(file.getName(), file.length(), file.getPath());
        return fileDto;
    }

}
