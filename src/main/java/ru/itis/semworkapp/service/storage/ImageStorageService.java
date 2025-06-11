package ru.itis.semworkapp.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageStorageService {
    String uploadFile(MultipartFile file) throws IOException;
    String getPresignedUrl(String key);
    List<String> uploadFiles(List<MultipartFile> files) throws IOException;
}
