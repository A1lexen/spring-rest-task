package com.example.springresttask.repository;

import com.example.springresttask.model.UploadedFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileRepository {
    List<UploadedFile> fileList;

    public void add(MultipartFile file) {
        byte[] fileData;
        String fileName = file.getOriginalFilename();
        int lastIndex = fileName.lastIndexOf('.');
        try{
            fileData = file.getBytes();
        }
        catch (IOException e){
            System.err.println("failed return a byte array of the file's contents.");
            e.printStackTrace();
            return;
        }
        fileList.add(new UploadedFile(
                fileList.size(),
                fileName,
                fileName.substring(lastIndex, fileName.length()),
                fileData,
                System.getProperty("user.dir") +
                        "\\src\\main\\resources\\uploadFiles\\" +
                        fileName));
    }

    public UploadedFile getById(int id) {
        return fileList.get(id);
    }

    public UploadedFile getByName(String fileName) {
        return
                fileList.stream().
                        filter(f -> f.getFileName().equals(fileName)).
                        findFirst().
                        get();
    }

}
