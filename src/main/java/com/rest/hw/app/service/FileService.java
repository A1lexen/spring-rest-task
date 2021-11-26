package com.rest.hw.app.service;

import com.rest.hw.app.entity.FileDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {
    String path = "//src//main//java//com//rest//app//";

    public void uploadFile(MultipartFile file) throws IOException {
        File file1 = new File(path);
        file.transferTo(file1);
    }

    public List<FileDTO> allFiles() {
        File folder = new File(path);
        List<File> files = List.of(folder.listFiles());
        List<FileDTO> fileList = new ArrayList<>();
        for (File file : files) {
            fileList.add(new FileDTO(file.getName(), (int) file.length(), file.getPath()));
        }
        return fileList;
    }

    public void deleteFile(MultipartFile file) throws IOException {
        File file1 = new File(path);
        if (file1.exists()) {
            file1.delete();
        } else {
            throw new FileNotFoundException();
        }
    }

    public InputStreamResource downloadFile(String name) throws java.io.FileNotFoundException {
        File file = new File(path);
        InputStreamResource newFile;
        if (file.exists()) {
            newFile = new InputStreamResource(new FileInputStream
                    (new File(path)));
        } else {
            throw new FileNotFoundException(name);
        }
        return newFile;
    }

    public void renameFile(String newName, String oldName) {
        File file1 = new File(path + oldName);
        file1.renameTo(new File(path + newName));
    }
}
