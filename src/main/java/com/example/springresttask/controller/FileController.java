package com.example.springresttask.controller;

import com.example.springresttask.model.UploadedFile;
import com.example.springresttask.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    FileService fileService;

    @GetMapping
    public String files(Model model) {
        List<UploadedFile> fileList = fileService.getFileList();
        model.addAttribute("files", fileList);
        return "/files";
    }

    @GetMapping("/upload")
    public String upload() {
        return "/upload";
    }

    @GetMapping("/download/{id}")
    public String downloadFile(@PathVariable Integer id) {
        fileService.downloadFile(id);
        return "redirect:/files";
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute("files", fileService.getFileList());
        return "/delete";
    }


    @GetMapping("/update")
    public String update(Model model) {
        model.addAttribute("files", fileService.getFileList());
        return "/update";
    }

    @PostMapping
    public String addFile(@RequestParam("file") MultipartFile file) {
        fileService.addFileToList(file);
        return "redirect:/files";
    }

    @PostMapping("/update")
    public String updateFile(@RequestParam("newFileName") String newFileName,
                             @RequestParam("oldFileName") String oldFileName) {
        fileService.updateFile(newFileName, oldFileName);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam("fileName") String fileName) {
        fileService.deleteFile(fileName);
        return "redirect:/";
    }

}
