package vn.ducbackend.exception.customException;

import vn.ducbackend.exception.AppException;

public class DuplicateException extends AppException {
    public DuplicateException(String message) {
        super("ERROR.DUPLICATE", message);
    }
}
