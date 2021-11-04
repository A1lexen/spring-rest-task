package com.example.demo.exeption;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String name) {
        super(String.format("File with name %n not found", name));
    }
}
