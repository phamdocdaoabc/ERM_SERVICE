package vn.ducbackend.exception.customException;

import vn.ducbackend.exception.AppException;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super("ERROR.NOT_FOUND", message);
    }
}
