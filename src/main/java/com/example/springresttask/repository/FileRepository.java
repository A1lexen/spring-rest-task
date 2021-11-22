package com.example.springresttask.repository;

import com.example.springresttask.model.UploadedFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileRepository {
    List<UploadedFile> fileList;

    public void add(MultipartFile file) {
        fileList.add(new UploadedFile(fileList.size(), file));
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
