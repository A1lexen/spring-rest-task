package com.example.demo.controllers;

import com.example.demo.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class FilesUploadController {
@Autowired
FileUploadService fileUploadService;

    @GetMapping("/all")
    public ResponseEntity<List<com.example.demo.model.File>> listFiles(){
        return ResponseEntity.status(200).body(fileUploadService.listFiles());
    }

    @GetMapping("/download")
    public ResponseEntity<?> fileDownload(@RequestParam("name") String name) throws IOException {

        InputStreamResource resource = fileUploadService.download(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .contentLength(new File(fileUploadService.getPath() + name).length())
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(resource);

        return responseEntity;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name)  {
        try {
            fileUploadService.fileUpload(file, name);
        } catch (IOException e) {
             return ResponseEntity.status(505).body("Try change file name");
        }
        return ResponseEntity.status(200).body("uploaded");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("name") String name) {
        try {
            fileUploadService.deleteFile(name);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file does not exist ");
        }
        return ResponseEntity.status(200).body("deleted");
    }

    @PutMapping("/set")
    public ResponseEntity<String> setFile(@RequestParam("name_new") String nameNew, @RequestParam("name_old") String nameOld) {
        try {
            fileUploadService.setFile(nameNew, nameOld);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("something wrong");
        }
        return ResponseEntity.status(200).build();
    }

}
