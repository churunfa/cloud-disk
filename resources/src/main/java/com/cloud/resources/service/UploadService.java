package com.cloud.resources.service;

import com.cloud.common.pojo.file.FileDB;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    FileDB saveFile(MultipartFile file);
}
