package com.example.demo.testingWeb;

import com.example.demo.model.FileData;
import com.example.demo.service.FileUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.core.io.InputStreamResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private FileUploadService fileUploadService;
    @Test
    public void notAuthorithedExpect401()throws Exception{
        this.mockMvc.perform(get("/all")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void checkgetAll() throws Exception{
        when(fileUploadService.listFiles()).thenReturn(mockedresponse());
        this.mockMvc.perform(get("/all")
                        .with(httpBasic("user","password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"t.txt\",\"size\":0,\"path\":\"/\"}]"));
    }

    @Test
    public void fileDownloadtest() throws Exception {
       when(fileUploadService.download(anyString())).thenReturn(mockedInputStream());

        this.mockMvc.perform(get("/download")
                        .param("name", "t.txt")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk());
    }

    @Test
    public void uploads() throws Exception {
        this.mockMvc.perform(post("/upload").
                with(httpBasic("admin", "password123")))
                .andExpect(status().isOk());
    }
    @Test
    public void delete() throws Exception {
      this.mockMvc.perform(MockMvcRequestBuilders.delete("/delete")
              .with(httpBasic("admin", "password123")))
              .andExpect(status().isOk());
    }

    @Test
    private void settest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/set")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk());
    }

    private List<FileData> mockedresponse(){
        List<FileData> list = new ArrayList<>();
        list.add(new FileData("t.txt", 0, "/"));
        return list;
    }


    private InputStreamResource mockedInputStream() throws FileNotFoundException {
        return new InputStreamResource(new FileInputStream(new File(System.getProperty("user.dir") + "/src/main/resources/store/t.txt")));
    }
}
