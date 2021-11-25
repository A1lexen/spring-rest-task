package com.task.demo.controller;

import com.task.demo.model.FileInfo;
import com.task.demo.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String message = "File was  uploaded";
        fileService.upload(multipartFile);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFile(@RequestParam("file") String filename) {
        fileService.getFile(filename);

        return ResponseEntity.status(HttpStatus.OK).body(fileService.getFile("file"));
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("file") String filename) {
        String message = "File was successfully deleted";
        fileService.deleteFile(filename);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateFile(@RequestParam("file") String filename, @RequestParam("newFile") String newFileName) {
        String message = "File was  updated";
        fileService.updateFile(filename,newFileName);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
