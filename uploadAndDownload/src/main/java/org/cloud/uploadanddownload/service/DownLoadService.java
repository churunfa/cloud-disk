package org.cloud.uploadanddownload.service;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.ZipFile;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DownLoadService {
    ResponseEntity<byte[]> download(Integer id, User user);

    ResponseEntity<byte[]> downloads(List<Integer> list, User user);

    ResponseEntity<byte[]> shareDownload(int id, String password, String name);
}
