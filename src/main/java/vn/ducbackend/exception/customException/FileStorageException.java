package vn.ducbackend.exception.customException;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message, Throwable cause) {

        super(message, cause);
    }
}
