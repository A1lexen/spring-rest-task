package com.resthw.resthwapp;

import com.rest.hw.app.entity.FileDTO;
import com.rest.hw.app.service.FileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class FileServiceTest {
    @Autowired
    private MockMvc mock;

    @Autowired
    @MockBean
    private FileService fileService;


    private InputStreamResource mockedInputStream() throws FileNotFoundException {
        return new InputStreamResource(new FileInputStream(new File(System.getProperty("user") + "/src/main/java/com/rest/app/")));
    }


    private List<FileDTO> mockedResponse(){
        List<FileDTO> list = new ArrayList<>();
        list.add(new FileDTO("dataFile", 0, "/"));
        return list;
    }

    @Test
    public void renameFileTest() throws Exception {
        mock.perform(put("/rename_file")
                .param("newName", "newName")
                .param("oldName", "oldName")
                .with(httpBasic("admin","ADMIN")))
                .andExpect(status().isOk());
    }
    @Test
    public void getAllFilesTest() throws Exception {
        when(fileService.allFiles()).thenReturn(mockedResponse());
        this.mock.perform(get("/all_files")
                .with(httpBasic("user", "USER")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void fileDownloadTest() throws Exception {
        when(fileService.downloadFile(anyString())).thenReturn(mockedInputStream());

        this.mock.perform(get("/download_file")
                .param("name", "dataFile")
                .with(httpBasic("admin", "ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void uploads() throws Exception {
        this.mock.perform(post("/upload_file").
                with(httpBasic("admin", "ADMIN")))
                .andExpect(status().isOk());
    }
    @Test
    public void delete() throws Exception {
        this.mock.perform(MockMvcRequestBuilders.delete("/delete_file")
                .with(httpBasic("admin", "ADMIN")))
                .andExpect(status().isOk());
    }
}
