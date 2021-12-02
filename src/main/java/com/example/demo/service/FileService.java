package com.example.demo.service;

import com.example.demo.entity.FileDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Value("${files}")
    private String path;

    private List<FileDto> fileList = new ArrayList<>();

    static List<FileDto> FILES = Lists.newArrayList(
            new FileDto("Secret", 1, "${files}"),
            new FileDto("Pentagon", 10, "${files}"),
            new FileDto("FBI", 100, "${files}")
    );

    public FileDto uploadFile(String name, MultipartFile file) throws Exception{
        path = path + name;
        File fileTemp = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(fileTemp);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(fileOutputStream);
                stream.write(bytes);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileDto tempFile = new FileDto(name, fileTemp, path);
        FILES.add(tempFile);
        return tempFile;
    }

    public List<FileDto> getFilesByName(String searchName){
        return FILES.stream()
                .filter(i -> i.getName().contains(searchName))
                .collect(Collectors.toList());
    }

    public List<FileDto> getAllFiles(){
        return FILES;
    }

    public List<FileDto> deleteFile(String name) throws IOException {
        List<FileDto> removedFiles = FILES.stream()
                .filter(fileDto -> fileDto.getName().equals(name))
                .collect(Collectors.toList());

        FILES.removeIf(fileDto -> fileDto.getName().equals(name));

        return removedFiles;
    }

    public FileDto changeFileName(String name, String newName) {
        return FILES.stream()
                .filter(fileDto -> fileDto.getName().equals(name))
                .findFirst()
                .map(fileDto -> {
                    fileDto.setName(newName);
                    return fileDto;
                })
                .orElseThrow(NullPointerException::new);
    }

    public ResponseEntity<Resource> downloadFile(String name) throws FileNotFoundException {

        FileDto file = FILES.stream()
                .filter(fileDto -> fileDto.getName().equals(name))
                .findFirst()
                .get();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+name);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file.getFile()));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.getFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
