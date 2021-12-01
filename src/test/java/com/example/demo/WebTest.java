package com.example.demo;

import com.example.demo.controller.FileController;
import com.example.demo.entity.FileDto;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.FileService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FileController.class)
public class WebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private FileRepository fileRepository;

    static List<FileDto> FILES = Lists.newArrayList(
            new FileDto("Secret", 1, "${files}"),
            new FileDto("Pentagon", 10, "${files}"),
            new FileDto("FBI", 100, "${files}")
    );

    @Test
    public void unauthorizedUser_test() throws Exception{
        mockMvc.perform(get("/file/allFiles")
            .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void getAllFiles_shouldSucceedWith200() throws Exception{
        mockMvc.perform(get("/file/allFiles")
                .with(httpBasic("admin","admin")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteFile_shouldSucceedWith403() throws Exception {
        mockMvc.perform(delete("/file/delete/Secret")
                .with(httpBasic("user","user")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteFile_shouldSucceed() throws Exception {
        mockMvc.perform(delete("/file/delete/Secret")
                .with(httpBasic("admin","admin")))
                .andExpect(status().isOk());
    }

    @Test
    public void searchFile_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/file/search/Secret")
                .with(httpBasic("user","user")))
                .andExpect(status().isOk());
    }

    @Test
    public void uploadMethod_shouldSucceedWith200() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload")
                .file(file)
                .param("name", "name")
                .with(httpBasic("admin","admin")))
                .andExpect(status().isOk());
    }

    @Test
    public void changeFileName_shouldSucceedWith200() throws Exception {
        mockMvc.perform(put("/file/changeFileName")
                .param("name", "name")
                .param("newName", "new_name")
                .with(httpBasic("admin","admin")))
                .andExpect(status().isOk());
    }

    @Test
    public void downloadFile_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/file/download/name")
                .with(httpBasic("admin","admin")))
                .andExpect(status().isOk());
    }

}
