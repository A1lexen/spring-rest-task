package co.inventorsoft.spring_rest_task.service;


import co.inventorsoft.spring_rest_task.exception.InvalidFileNameException;
import co.inventorsoft.spring_rest_task.exception.MyFileNotFoundException;
import co.inventorsoft.spring_rest_task.payload.FileResponse;
import co.inventorsoft.spring_rest_task.property.FileStorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {
    private final Path fileStorageLocation;

    private void validateFilename(String fileName) {
        String fileNameRegex = "^[a-zA-Z0-9](?:[a-zA-Z0-9 ._-]*[a-zA-Z0-9])?\\.[a-zA-Z0-9_-]+$";
        if (!fileName.matches(fileNameRegex)) {
            throw new InvalidFileNameException(fileName);
        }
    }
    public FileService(FileStorageProperties fileStorageProperties) {
        // get path from application.properties
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        // try to create directories
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("could not create the directories.", ex);
        }
    }

    public String store(MultipartFile file) {
        // normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        validateFilename(fileName);
        try {
            // copy file to the target directory
            Path targetDirectory = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetDirectory, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("could not store file " + fileName + ".", e);
        }
    }

    public Resource load(String fileName) {
        try {
            // get path from application.properties and filename
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            else {
                throw new MyFileNotFoundException("file " + fileName + " not found.");
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("file " + fileName + " not found.", ex);
        }
    }

    public boolean update(String oldFileName, String newFileName) throws IOException {
        validateFilename(newFileName);
        //create new file here
        Path filePath = fileStorageLocation.resolve(newFileName).normalize();

        File oldFile = load(oldFileName).getFile();
        File newFile = new File(filePath.toUri());

        if (!newFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }

    public boolean delete(String fileName) throws IOException {
        return load(fileName).getFile().delete();
    }

    /**
     * Try to determine file content type
     */
    public String getContentType(Resource resource, ServletContext servletContext) {
        try {
            return servletContext.getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }

    public String getDownloadUri(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileName)
                .toUriString();
    }

    public FileResponse uploadFile(MultipartFile file) {
        String fileName = store(file);
        String fileDownloadUri = getDownloadUri(fileName);
        return new FileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    public FileResponse infoOfFile(String fileName, ServletContext servletContext) throws IOException {
        // Load file as Resource
        Resource resource = load(fileName);

        // Try to determine file content type
        String contentType = getContentType(resource, servletContext);

        String fileDownloadUri = getDownloadUri(fileName);
        return new FileResponse(fileName, fileDownloadUri, contentType, resource.getFile().length());
    }
}
