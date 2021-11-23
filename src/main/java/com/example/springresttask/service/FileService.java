package com.example.springresttask.service;

import com.example.springresttask.model.UploadedFile;
import com.example.springresttask.repository.FileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            log.error("failed downloading file", e);
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
        String folderPath = System.getProperty("user.dir") + "\\src\\main\\resources\\uploadFiles\\";
        File newFile = new File(folderPath + newFileName);

        try {
            log.info("Creating a new file...");
            newFile.createNewFile();
            log.info("opening a new file output stream");
            FileOutputStream fos = new FileOutputStream(newFile);
            log.info("file content writing");
            fos.write(getFileByName(oldFileName).getFileData());
            log.info("closing FOS");
            fos.close();
        } catch (IOException e) {
            log.error("", e);
        }

        File oldFile = new File(folderPath + oldFileName);
        oldFile.delete();

        getFileByName(oldFileName).setFileName(newFileName);
    }

    public void deleteFile(String fileName) {
        File file = new File(System.getProperty("user.dir") +
                "\\src\\main\\resources\\uploadFiles\\" +
                getFileByName(fileName));
        file.delete();
        fileRepository.getFileList().remove(fileRepository.getByName(fileName).getFileId());
    }
}
