package com.example.resttask;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.resttask.controller.FileController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestTaskApplicationTests {

    @Autowired
    private FileController fileController;

    @Test
    void contextLoads() {
        assertThat(fileController).isNotNull();
    }

}
