package com.cloud.resources.service;

import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.FileDB;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    FileDB saveFile(MultipartFile file);
    Chunk chunkUpload(MultipartFile file, Integer user_file_id, Integer chunk_number);
    FileDB merge(Integer user_file_id, Long chunk_size, Long tot_size, String name);
}
