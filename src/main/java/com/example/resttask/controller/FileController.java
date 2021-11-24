package com.example.resttask;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileController {

    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam String name, @RequestParam MultipartFile file) {
        fileService.uploadFile(name, file);
        return ResponseEntity.status(HttpStatus.OK).body("File successfully uploaded");
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String name) throws FileNotFoundException {
        InputStreamResource resource = fileService.downloadFile(name);
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDto>> listFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.listFiles());
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getFile(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.getFile(name));
    }

    @DeleteMapping("/delete/")
    public ResponseEntity<?> deleteFile(@RequestParam String name) {
        fileService.deleteFile(name);
        return ResponseEntity.status(HttpStatus.OK).body("File successfully uploaded");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateFile(@RequestParam String name, @RequestParam String newName) {
        fileService.updateFile(name, newName);
        return ResponseEntity.status(HttpStatus.OK).body("File successfully updated");
    }

}
