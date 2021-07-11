package com.cloud.resources.service;

import com.cloud.common.pojo.file.ZipFile;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DownloadService {
    ResponseEntity<byte[]> downloadByPath(Integer id, String filename);
    ResponseEntity<byte[]> downloadByPath(String path, String filename);
    ResponseEntity<byte[]> downloadByPaths(ZipFile zipFile);
}
