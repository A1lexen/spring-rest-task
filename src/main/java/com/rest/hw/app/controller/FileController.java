package com.rest.hw.app.controller;

import com.rest.hw.app.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class FileController {
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> fileUpload(MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body("uploaded");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(MultipartFile file) throws IOException {
        fileService.deleteFile(file);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }

    @PutMapping("/rename")
    public ResponseEntity<String> changeFile(String nameNew, String nameOld) {
        fileService.renameFile(nameNew, nameOld);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
