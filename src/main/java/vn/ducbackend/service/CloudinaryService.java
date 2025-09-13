package vn.ducbackend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadFile(MultipartFile file);
    void deleteFile(String publicId);
}
