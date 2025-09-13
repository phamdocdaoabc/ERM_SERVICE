package vn.ducbackend.exception.customException;

public class CloudinaryException extends RuntimeException {
    public CloudinaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
