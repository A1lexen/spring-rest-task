package com.homework.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.homework.demo.service.FilesStorageService;

import javax.annotation.Resource;


@SpringBootApplication
public class Homework7Application implements CommandLineRunner{
    @Resource
    FilesStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(Homework7Application.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        storageService.deleteAll();
        storageService.init();
    }
}
