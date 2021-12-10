package com.example.springresttask.service;

import com.example.springresttask.model.PathToUploadedFiles;
import com.example.springresttask.model.UploadedFile;
import com.example.springresttask.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public boolean addFileToList(MultipartFile file) {
        return fileRepository.add(file);
    }

    public ResponseEntity uploadFile(MultipartFile file) {
        Path path = Paths.get(PathToUploadedFiles.getRoot() + "\\" + file.getOriginalFilename());
        try {
            Files.write(path, file.getBytes());
            addFileToList(file);
        } catch (IOException e) {
            log.error("failed downloading file", e);
            return new ResponseEntity<>("Root folder or file not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("The file uploaded successfully", HttpStatus.OK);

    }

    public List<UploadedFile> getFileList() {
        return fileRepository.getFileList();
    }

    public UploadedFile getFileByName(String fileName) {
        return fileRepository.getByName(fileName);
    }

    public boolean isFileWithThisNameExist(String fileName) {
        return fileRepository.isFileWithThisNameExist(fileName);
    }

    public ResponseEntity updateFile(String newFileName, String oldFileName) {
        if (!isFileWithThisNameExist(oldFileName)) {
            return new ResponseEntity<>("File with this name not found", HttpStatus.NOT_FOUND);
        }
        File newFile = new File(PathToUploadedFiles.getRoot() + "\\" + newFileName);

        log.info("opening a new file output stream");
        try(FileOutputStream fos = new FileOutputStream(newFile)) {
            log.info("Creating a new file...");
            newFile.createNewFile();
            log.info("file content writing");
            fos.write(getFileByName(oldFileName).getFileData());
            log.info("closing FOS");
        } catch (IOException e) {
            log.error("failed try to update file.", e);
            if (newFile.exists()) newFile.delete();
            return new ResponseEntity<>("File was not updated. ", HttpStatus.BAD_GATEWAY);
        }

        File oldFile = new File(PathToUploadedFiles.getRoot() + "\\" + oldFileName);
        oldFile.delete();

        getFileByName(oldFileName).setFileName(newFileName);
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
    }

    public ResponseEntity deleteFile(String fileName) {
        if (!isFileWithThisNameExist(fileName)) {
            new ResponseEntity("File with this name was not found", HttpStatus.NOT_FOUND);
        }
        File file = new File(PathToUploadedFiles.getRoot() + "\\" + fileName);
        if (file.delete()) {
            fileRepository.getFileList().remove(fileRepository.getByName(fileName).getFileId());
            return new ResponseEntity("Successful delete", HttpStatus.OK);
        } else {
            log.error("File.delete return false");
            return new ResponseEntity("File.delete return false", HttpStatus.BAD_GATEWAY);
        }
    }


}
