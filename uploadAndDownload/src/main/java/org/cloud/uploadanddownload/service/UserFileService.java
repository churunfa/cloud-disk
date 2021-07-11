package org.cloud.uploadanddownload.service;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.ZipFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public interface UserFileService {
    Map upload(MultipartFile file, User user, String dir);
    ResponseEntity<byte[]>  downloads(ZipFile zipFile);
    ResponseEntity<byte[]> download(Integer id, String filename);
}
