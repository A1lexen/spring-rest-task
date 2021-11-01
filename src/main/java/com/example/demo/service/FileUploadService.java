package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileUploadService {
    private String path = "//home//ivan//Завантаження//Нова тека//Нова тека//";
    public List<com.example.demo.model.File> listFiles(){
        File folder = new File(path);
        List<File> files = List.of(folder.listFiles());
        List<com.example.demo.model.File> fileList = new ArrayList<>();
        for (File f:files) {
           fileList.add(new com.example.demo.model.File(f.getName(), (int) f.length(), f.getPath()));
        }
        return fileList;
    }

    public String getPath() {
        return path;
    }

    public void fileUpload(MultipartFile file, String name) throws IOException {
       file.transferTo(new File(path + name));
    }

    public void deleteFile(String file) throws IOException {
        Files.deleteIfExists(Path.of(path, file));
    }

    public void setFile(String file_name, String old_name) throws IOException {
       File file = new File(path+old_name);
       file.renameTo(new File(path+file_name));
    }

    public InputStreamResource download(String name) throws FileNotFoundException {
        return new InputStreamResource(new FileInputStream(new File(path, name)));
    }


}
