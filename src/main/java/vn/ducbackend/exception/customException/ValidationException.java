package vn.ducbackend.exception.customException;

import vn.ducbackend.exception.AppException;

public class ValidationException extends AppException {
    public ValidationException(String message) {
        super("ERROR.VALIDATION", message);
    }
}
