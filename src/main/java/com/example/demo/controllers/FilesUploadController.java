package com.example.demo.controllers;

import com.example.demo.model.FileData;
import com.example.demo.service.FileUploadService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class FilesUploadController {

    FileUploadService fileUploadService;

    @GetMapping("/all")
    public ResponseEntity<List<FileData>> listFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(fileUploadService.listFiles());
    }

    @GetMapping("/download")
    public ResponseEntity<?> fileDownload(String name) throws IOException {

        InputStreamResource resource = fileUploadService.download(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileUploadService.length(name))
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(resource);

        return responseEntity;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> fileUpload(MultipartFile file, String name) {
        fileUploadService.fileUpload(file, name);
        return ResponseEntity.status(HttpStatus.OK).body("uploaded");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(String name) throws IOException {
        fileUploadService.deleteFile(name);

        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }

    @PutMapping("/set")
    public ResponseEntity<String> setFile(String nameNew, String nameOld) {

        fileUploadService.setFile(nameNew, nameOld);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
