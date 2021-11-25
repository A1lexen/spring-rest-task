package com.rest.hw.app.controller;

import com.rest.hw.app.entity.FileDTO;
import com.rest.hw.app.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class FileController {
    FileService fileService;

    @PostMapping(
            value = "/upload_file",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> fileUpload(@RequestBody MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body("uploaded");
    }

    @DeleteMapping(value = "/delete_file", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteFile(@RequestBody MultipartFile file) throws IOException {
        fileService.deleteFile(file);
        new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PutMapping(value = "/rename_file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeFile(@RequestBody String nameNew, String nameOld) {
        fileService.renameFile(nameNew, nameOld);
       return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/all_files", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileDTO>> listFiles() {
        return new ResponseEntity<>(fileService.allFiles(), HttpStatus.OK);
    }

    @GetMapping(value = "/download_file")
    public void downloadFile(@RequestParam String name) throws FileNotFoundException {
        InputStreamResource resource = fileService.downloadFile(name);
         ResponseEntity.status(HttpStatus.OK).body(resource);
    }

}
