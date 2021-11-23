package com.rest.hw.app.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
@AllArgsConstructor
public class FileService {
    PersonService personService;
    String path = "//src//main//java//com//rest//app//";

    public void writer() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("dataFile", true));
            writer.write("\n" + personService.getAll() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(MultipartFile file) throws IOException {
        writer();
        File file1 = new File(path);
        file.transferTo(file1);
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
        if (!file.exists()) {
            throw new FileNotFoundException(name);
        }
        return new InputStreamResource(new FileInputStream
                (new File(path)));
    }


    public void renameFile(String newName, String oldName) {
        File file1 = new File(path + oldName);
        file1.renameTo(new File(path + newName));
    }
}
