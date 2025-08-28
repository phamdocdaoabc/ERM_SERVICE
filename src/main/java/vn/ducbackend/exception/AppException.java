package vn.ducbackend.exception;

public class AppException extends RuntimeException{
    private final String code; // ví dụ: DUPLICATE, NOT_FOUND

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
