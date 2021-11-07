package com.example.demo.controller;


import com.example.demo.entity.FileDto;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
public class FileController {


    private FileService fileService;

    @Autowired
    public FileController(FileService fileService){
        this.fileService = fileService;
    }


    @PostMapping("/upload")
    public FileDto uploadFile(@RequestParam String name, @RequestParam MultipartFile file) throws Exception{
        return fileService.uploadFile(name, file);
    }

    @GetMapping("/allFiles")
    public List<FileDto> allFiles(){
        return fileService.getAllFiles();
    }

    @GetMapping("/search")
    public List<FileDto> searchForFiles(@RequestParam String name){
        return fileService.getFilesByName(name);
    }

    @DeleteMapping("/delete/{name}")
    public List<FileDto> deleteFile(@PathVariable("name") String name) throws IOException {
        return fileService.deleteFile(name);
    }

    @PutMapping("/changeFileName")
    public FileDto changeFileName(@RequestParam String name, @RequestParam String newName){
        return fileService.changeFileName(name, newName);
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("name") String name) throws FileNotFoundException {
        return fileService.downloadFile(name);
    }

}
