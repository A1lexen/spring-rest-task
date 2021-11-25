package com.task.demo.service;

import com.task.demo.model.FileInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileService implements FileServiceI{


    private final Path sourcePath = Paths.get("directory");



    @Override
    public void upload(MultipartFile multipartFile) {
        try {
            Files.copy(multipartFile.getInputStream(), this.sourcePath.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public FileInfo getFile(String filename) {
        File file = new File(sourcePath + filename);
        FileInfo fileInfo = new FileInfo(file.getName(), file.length(), file.getPath());
        return fileInfo;
    }

    @Override
    public void deleteFile(String filename) {
        File file = new File(sourcePath + filename);
        file.delete();

    }

    @Override
    public void updateFile(String existNameFile, String newNameFile) {

        Path file = sourcePath.resolve(existNameFile);
        Path newFile = sourcePath.resolve(newNameFile);

        try {
            Files.move(file, newFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
