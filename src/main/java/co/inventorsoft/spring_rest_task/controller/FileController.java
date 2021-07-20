package co.inventorsoft.spring_rest_task.controller;

import co.inventorsoft.spring_rest_task.payload.FileResponse;
import co.inventorsoft.spring_rest_task.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        FileResponse fileResponse = fileService.uploadFile(file);
        return ResponseEntity.ok().body(fileResponse);
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.load(fileName);

        // Try to determine file content type
        String contentType = fileService.getContentType(resource, request.getServletContext());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PatchMapping("/files/{fileName:.+}")
    public ResponseEntity<String> updateFileName(@PathVariable String fileName, @RequestParam("name") String name) throws IOException {
        boolean updated = fileService.update(fileName, name);
        return ResponseEntity.ok(updated ? "File was updated successfully" : "File wasn't updated");
    }

    @DeleteMapping("/files/{fileName:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) throws IOException {
        boolean deleted = fileService.delete(fileName);
        return ResponseEntity.ok().body(deleted ? "File was deleted successfully" : "File wasn't deleted");
    }

    @GetMapping("/files/{fileName:.+}/info")
    public ResponseEntity<FileResponse> infoOfFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        FileResponse fileResponse = fileService.infoOfFile(fileName, request.getServletContext());
        return ResponseEntity.ok().body(fileResponse);
    }

}
