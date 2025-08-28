package vn.ducbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.exception.customException.ValidationException;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // khi entity không tồn tại
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .message("An Unexpected Error Occurred!")
                        .traceId(UUID.randomUUID().toString())
                        .errorCodes(ErrorDetail.builder()
                                .code(ex.getCode())
                                .message(ex.getMessage())
                                .build())
                        .build());
    }

    // khi check trùng
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleDuplicate(DuplicateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.builder()
                        .message("An Unexpected Error Occurred!")
                        .traceId(UUID.randomUUID().toString())
                        .errorCodes(ErrorDetail.builder()
                                .code(ex.getCode())
                                .message(ex.getMessage())
                                .build())
                        .build());
    }

    // khi dữ liệu không hợp lệ (logic business).
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .message("An Unexpected Error Occurred!")
                        .traceId(UUID.randomUUID().toString())
                        .errorCodes(ErrorDetail.builder()
                                .code(ex.getCode())
                                .message(ex.getMessage())
                                .build())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage(); // lấy message đầu tiên
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .message("An Unexpected Error Occurred!")
                        .traceId(UUID.randomUUID().toString())
                        .errorCodes(ErrorDetail.builder()
                                .code("ERROR.INVALID")
                                .message(errorMessage)
                                .build())
                        .build());
    }
}
