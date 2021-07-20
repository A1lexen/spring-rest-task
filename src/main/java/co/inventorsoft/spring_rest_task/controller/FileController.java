package co.inventorsoft.spring_rest_task.controller;


import co.inventorsoft.spring_rest_task.payload.FileResponse;
import co.inventorsoft.spring_rest_task.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        String fileName = fileService.store(file);
        String fileDownloadUri = fileService.getDownloadUri(fileName);
        return ResponseEntity.ok()
                .body(new FileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize()));
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.load(fileName);

        // Try to determine file's content type
        String contentType = fileService.getContentType(resource, request.getServletContext());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PatchMapping("/files/{fileName:.+}")
    public ResponseEntity<String> updateFileName(@PathVariable String fileName, @RequestParam("name") String name) throws IOException {
        boolean updated = fileService.update(fileName, name);
        String message = updated ? "file was updated successfully" : "file was NOT updated successfully";
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/files/{fileName:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) throws IOException {
        boolean deleted = fileService.delete(fileName);
        String message = deleted ? "file was deleted successfully" : "file was NOT deleted successfully";
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/files/{fileName:.+}/info")
    public ResponseEntity<FileResponse> infoOfFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = fileService.load(fileName);

        // Try to determine file's content type
        String contentType = fileService.getContentType(resource, request.getServletContext());

        String fileDownloadUri = fileService.getDownloadUri(fileName);
        return ResponseEntity.ok()
                .body(new FileResponse(fileName, fileDownloadUri, contentType, resource.getFile().length()));
    }

}
