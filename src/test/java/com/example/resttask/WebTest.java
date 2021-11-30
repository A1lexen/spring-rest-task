package com.example.resttask;

import com.example.resttask.model.FileDto;
import com.example.resttask.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileService fileService;

    @Test
    public void shouldBeUnauthorized() throws Exception {
        this.mockMvc.perform(get("/files"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user")
    public void shouldBeForbidden() throws Exception {
        this.mockMvc.perform(post("/upload"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    public void listFilesTest() throws Exception {
        when(fileService.listFiles()).thenReturn(List.of(new FileDto("file.txt", 10, "/")));
        this.mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"file.txt\",\"size\":10,\"path\":\"/\"}]"));
    }

    @Test
    @WithUserDetails("admin")
    public void updateFileTest() throws Exception {
        this.mockMvc.perform(post("/update").content("{ \"name\": \"file.txt\", \"newName\": \"newFile\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("File successfully updated"));
    }

    @Test
    @WithUserDetails("user")
    public void getFileTest() throws Exception {
        when(fileService.getFile("file.txt")).thenReturn(new FileDto("file.txt", 10, "/"));
        this.mockMvc.perform(get("/{name}", "file.txt"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"file.txt\",\"size\":10,\"path\":\"/\"}"));
    }

    @Test
    @WithUserDetails("user")
    public void downloadFileTest() throws Exception {
        when(fileService.downloadFile(anyString())).thenReturn(getResource());
        this.mockMvc.perform(get("/download").param("name","file.txt"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void deleteFileTest() throws Exception {
        this.mockMvc.perform(delete("/delete").param("name", "file.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string("File successfully deleted"));
    }

    InputStreamResource getResource() throws FileNotFoundException {
        return new InputStreamResource(new FileInputStream(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\file.txt")));
    }

}
