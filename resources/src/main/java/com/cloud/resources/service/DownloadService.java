package com.cloud.resources.service;

import com.cloud.common.model.RestResult;
import com.cloud.common.pojo.file.ZipFile;
import com.cloud.common.pojo.file.ZipFileChunk;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DownloadService {
    ResponseEntity<byte[]> downloadByPath(Integer id, String filename);
    ResponseEntity<byte[]> downloadByPath(String path, String filename);
    ResponseEntity<byte[]> downloadByPaths(ZipFile zipFile);
    RestResult<byte[]> chunkDownload(int fid, int chunkSize, int chunkNo);
    RestResult<byte[]> chunkDownloadByPaths(ZipFileChunk zipFile);
    RestResult<long[]> getZip(ZipFileChunk zipFile);
    RestResult deleteZip(int takeId);
    ZipFileChunk check(String id);
}
