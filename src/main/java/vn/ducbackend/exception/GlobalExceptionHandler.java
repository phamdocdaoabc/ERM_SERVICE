package vn.ducbackend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.exception.customException.*;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(ApiErrorResponse.builder()
                        .message("Duplicate key or constraint violation")
                        .traceId(UUID.randomUUID().toString())
                        .errorCodes(ErrorDetail.builder()
                                .code("DUPLICATE_KEY")
                                .message(ex.getMostSpecificCause().getMessage()) // lấy chi tiết DB trả về
                                .build())
                        .build());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleFileStorage(FileStorageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .message("File error")
                        .errorCodes(ErrorDetail.builder()
                                .code("FILE_UPLOAD_ERROR")
                                .message(ex.getMessage())
                                .build())
                        .traceId(UUID.randomUUID().toString())
                        .build());
    }

    @ExceptionHandler(CloudinaryException.class)
    public ResponseEntity<ApiErrorResponse<Object>> handleCloudinary(CloudinaryException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.builder()
                        .message("Cloudinary upload error")
                        .errorCodes(ErrorDetail.builder()
                                .code("CLOUDINARY_ERROR")
                                .message(ex.getMessage())
                                .build())
                        .traceId(UUID.randomUUID().toString())
                        .build());
    }
}
