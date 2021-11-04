package com.example.demo.service;

import com.example.demo.exeption.FileNotFoundException;
import com.example.demo.exeption.NoDataFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileUploadService {
    String path = System.getProperty("user.dir") + "//src//main//resources//store//";

    public List<com.example.demo.model.FileData> listFiles() {
        File folder = new File(path);
        List<File> files = List.of(folder.listFiles());
        List<com.example.demo.model.FileData> fileList = new ArrayList<>();
        if(files.isEmpty()) throw new NoDataFoundException();
        for (File f : files) {
            fileList.add(new com.example.demo.model.FileData(f.getName(), (int) f.length(), f.getPath()));
        }
        return fileList;
    }

    public Long length(String name) {
        return new File(path + name).length();
    }

    public void fileUpload(MultipartFile file, String name)  {
        File file1 = new File(path + name);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            throw new NoDataFoundException();
        }
    }

    public void deleteFile(String file) throws IOException {

            if(!Files.deleteIfExists(
                    Path.of(path, file))) throw new FileNotFoundException(file);

    }


    public void setFile(String file_name, String old_name) {
        File file = new File(path + old_name);
        file.renameTo(new File(path + file_name));
    }

    public InputStreamResource download(String name) throws java.io.FileNotFoundException {
        File file = new File(path, name);
        if (!file.exists()) throw new FileNotFoundException(name);
        return new InputStreamResource(new FileInputStream(new File(path, name)));
    }


}
