package vn.ducbackend.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ducbackend.exception.customException.CloudinaryException;
import vn.ducbackend.exception.customException.FileStorageException;
import vn.ducbackend.service.CloudinaryService;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            // Bắt lỗi IO khi đọc file
            throw new FileStorageException("Failed to upload file: " + file.getOriginalFilename(), e);
        } catch (Exception e) {
            // Bắt tất cả lỗi khác từ Cloudinary
            throw new CloudinaryException("Error occurred while uploading to Cloudinary", e);
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            // Bắt lỗi IO khi đọc file
            throw new FileStorageException("Failed to upload file: " + publicId, e);
        } catch (Exception e) {
            // Bắt tất cả lỗi khác từ Cloudinary
            throw new CloudinaryException("Error occurred while uploading to Cloudinary", e);
        }
    }
}
