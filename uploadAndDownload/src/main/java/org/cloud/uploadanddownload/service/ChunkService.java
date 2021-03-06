package org.cloud.uploadanddownload.service;

import com.cloud.common.model.RestResult;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Chunk;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ChunkService {
    Chunk chunkUpload_(MultipartFile file, Integer user_file_id, User user, Integer chunkNo);
    Chunk chunkUpload_(MultipartFile file, Integer user_file_id, User user, Integer chunkNo, Integer chunkSize);
    Chunk chunkUpload(MultipartFile file, Integer user_file_id, User user, Integer chunkNo);
    Chunk chunkUpload(MultipartFile file, Integer user_file_id, User user, Integer chunkNo, Integer chunkSize);
    RestResult merge(Integer user_file_id, Long chunk_size, User user);
    RestResult merge(Integer user_file_id, User user);
    Integer getFid(String filename, Long size, String dir, User user);
    List<Map> getUploading(User user);
    Boolean cancel(Integer user_file_id, User user);
    Integer getNext(Integer user_file_id, User user);
    void download(Integer id, User user, HttpServletResponse httpServletResponse);
    void downloads(List<Integer> list, User user, HttpServletResponse response);

    void shareDownload(int id, String password, String filename, HttpServletResponse response);
    RestResult<String> check(int id, String password, String filename);
}
