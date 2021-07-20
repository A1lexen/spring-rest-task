package co.inventorsoft.spring_rest_task.exception;

public class InvalidFileNameException extends RuntimeException {
    public InvalidFileNameException(String filename) {
        super("Filename " + filename + "invalid");
    }
}
