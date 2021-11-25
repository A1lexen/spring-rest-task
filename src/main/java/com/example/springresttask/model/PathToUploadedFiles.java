package com.example.springresttask.model;

import java.nio.file.Path;

public class PathToUploadedFiles {
    private static final Path root;

    static {
        root = Path.of(
                System.getProperty("user.dir") +
                        "\\src\\main\\resources\\uploadFiles");
    }

    public static Path getRoot() {
        return root;
    }
}
