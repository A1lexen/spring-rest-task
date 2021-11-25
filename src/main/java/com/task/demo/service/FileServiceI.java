package com.task.demo.service;

import com.task.demo.model.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceI {



    public void upload(MultipartFile multipartFile);

    public FileInfo getFile(String filename);

    public void deleteFile(String filename);

    public void updateFile(String existNameFile, String newNameFile);




}
