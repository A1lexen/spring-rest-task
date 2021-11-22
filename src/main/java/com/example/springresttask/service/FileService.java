package com.example.springresttask.service;

import com.example.springresttask.model.UploadedFile;
import com.example.springresttask.repository.FileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileService {
    FileRepository fileRepository;

    public void addFileToList(MultipartFile file) {
        fileRepository.add(file);
    }

    public void downloadFile(int id) {
        Path path = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\uploadFiles\\" + fileRepository.getById(id).getFileName());
        try {
            Files.write(path, fileRepository.getById(id).getFileData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<UploadedFile> getFileList() {
        return fileRepository.getFileList();
    }

    public UploadedFile getFileByName(String fileName) {
        return fileRepository.getByName(fileName);
    }

    public void updateFile(String newFileName, String oldFileName) {
        //Create new file with new name
        File newFile = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\uploadFiles\\" +
                newFileName);

        try {
            newFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(getFileByName(oldFileName).getFileData());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Delete old file
        File oldFile = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\uploadFiles\\" +
                oldFileName);
        oldFile.delete();

        //update list
        getFileByName(oldFileName).setFileName(newFileName);
    }

    public void deleteFile(String fileName) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\uploadFiles\\" + getFileByName(fileName));
        System.out.println(file.delete());

        fileRepository.getFileList().remove(fileRepository.getByName(fileName).getFileId());
    }
}
