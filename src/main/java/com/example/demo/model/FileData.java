package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FileData {

   @Getter  private String name;
   @Getter  private int size;
    @Getter private String path;


}
