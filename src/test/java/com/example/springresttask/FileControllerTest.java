package com.example.springresttask;

import com.example.springresttask.configs.PasswordConfig;
import com.example.springresttask.configs.SecurityConfig;
import com.example.springresttask.controller.FileController;
import com.example.springresttask.model.UploadedFile;
import com.example.springresttask.service.FileService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
@Import({SecurityConfig.class, PasswordConfig.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    @MockBean
    FileService fileService;

    @Test
    public void deniedUnauthorizedRequestToURL() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authorizedRequestShouldBeAccessible() throws Exception {
        this.mockMvc.perform(get("/files")
                        .with(httpBasic("admin", "admin")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void successfullyUploadFileTest() throws Exception {
        when(fileService.uploadFile(any())).thenReturn(ResponseEntity.ok("The file was successfully upload"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "12345".getBytes());

        mockMvc.perform(multipart("/files/upload").file(mockMultipartFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("The file was successfully upload"));
    }

    @Test
    @WithUserDetails("admin")
    public void successfullyUpdatingFileTest() throws Exception {
        mockMvc.perform(post("/files/update").param("newFileName", "file.txt").param("oldFileName", "hello.txt"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void successfullyDeletingFile() throws Exception {
        mockMvc.perform(delete("/files/delete").param("fileName", "hello.txt"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void successfullyShowingFileList() throws Exception {
        String path = "C:/Users/zhlob/OneDrive/Рабочий стол/spring-rest-task/src/main/resources/uploadFiles/hello.txt";
        when(fileService.getFileList()).thenReturn(List.of(new UploadedFile(0, "hello.txt", ".txt", "12345".getBytes(), path)));
        mockMvc.perform(get("/files"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("[{\"fileId\":0,\"fileName\":\"hello.txt\",\"fileType\":\".txt\",\"fileData\":\"MTIzNDU=\",\"path\":\"%s\"}]", path)));
    }
}