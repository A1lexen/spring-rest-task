package com.example.demo.security;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ApplicationUserPermission {
    Read_Files("read:files"),
    Write_Files("write:files");

    private final String permission;
}
