package com.example.demo.security;

public enum ApplicationUserPermission {
    Read_Files("read:files"),
    Write_Files("write:files");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
