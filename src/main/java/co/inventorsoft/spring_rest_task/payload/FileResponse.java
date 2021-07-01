package co.inventorsoft.spring_rest_task.payload;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  this class is used to return the response from the /upload path
 */
@Data
@AllArgsConstructor
public class FileResponse {
    String fileName;
    String fileDownloadUri;
    String fileType;
    long size;
}
