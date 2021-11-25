package com.example.springresttask.controller;

import com.example.springresttask.model.UploadedFile;
import com.example.springresttask.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    FileService fileService;

    @GetMapping
    public List<UploadedFile> files() {
        return fileService.getFileList();
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @PostMapping("/update")
    public ResponseEntity updateFile(@RequestParam String newFileName,
                                     @RequestParam String oldFileName) {
        return fileService.updateFile(newFileName, oldFileName);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteFile(@RequestParam String fileName) {
        return fileService.deleteFile(fileName);
    }

}
