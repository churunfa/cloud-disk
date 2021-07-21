package org.cloud.uploadanddownload.service;

import com.cloud.common.model.RestResult;
import com.cloud.common.model.RestResultUtils;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.FileType;
import com.cloud.common.pojo.file.UserFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.cloud.uploadanddownload.controller.ResourcesService;
import org.cloud.uploadanddownload.mapper.ChunkMapper;
import org.cloud.uploadanddownload.mapper.UserFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.*;

@Service
public class ChunkServiceImpl implements ChunkService{

    UserFileMapper userFileMapper;

    ChunkMapper chunkMapper;

    ResourcesService resourcesService;

    @Autowired
    public void setUserFileMapper(UserFileMapper userFileMapper) {
        this.userFileMapper = userFileMapper;
    }

    @Autowired
    public void setResourcesService(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Autowired
    public void setChunkMapper(ChunkMapper chunkMapper) {
        this.chunkMapper = chunkMapper;
    }

    @Override
    public Chunk chunkUpload(MultipartFile file, Integer user_file_id, User user, Integer chunkNo) {
        return chunkUpload(file, user_file_id, user, chunkNo,5 * 1024 * 1024);
    }

    @Override
    public Chunk chunkUpload(MultipartFile file, Integer user_file_id, User user, Integer chunkNo, Integer chunkSize) {
        try (InputStream is = file.getInputStream()) {
            byte[] bytes = new byte[chunkSize];

            BufferedInputStream bis = new BufferedInputStream(is);

            bis.skip(chunkNo * chunkSize);

            int len = bis.read(bytes);

            if (len == -1) return null;

            FileItemFactory factory = new DiskFileItemFactory(16, null);

            FileItem item = factory.createItem("file", file.getContentType(), true, String.format("%d-%s", chunkNo, file.getOriginalFilename()));

            OutputStream os = item.getOutputStream();
            os.write(bytes, 0, len);
            os.close();

            MultipartFile multipartFile = new CommonsMultipartFile(item);
            System.out.println(multipartFile.getOriginalFilename());
            System.out.println(multipartFile.getSize());

            UserFile userFile = new UserFile();
            userFile.setId(user_file_id);
            userFile.setUser(user);
            userFile.setFile(new FileDB());
            userFile.setFile_name(file.getOriginalFilename());
            userFile.setGmt_modified(new Date());
            userFile.setSize(file.getSize());

            System.out.println(file.getSize());
            System.out.println(userFile);

            userFileMapper.updateUserFile(userFile);

            System.out.println(multipartFile.getOriginalFilename());
            RestResult<Chunk> upload = resourcesService.chunkUpload(multipartFile, user_file_id, chunkNo);

            int count = 1;
            for (int i = 0; i < 100; i++) {
                if (upload.ok()) break;
                count ++;
                upload = resourcesService.chunkUpload(multipartFile, user_file_id, chunkNo);
            }

            System.out.println("尝试上传 " + count + " 次");

            System.out.println(upload);
            return upload.getData();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public RestResult merge(Integer user_file_id, Long chunk_size, User user) {
        UserFile userFile = new UserFile();
        userFile.setId(user_file_id);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = userFileMapper.queryALLByUserFile(userFile);

        if (userFiles.size() != 1) return RestResultUtils.failed();

        userFile = userFiles.get(0);

        return resourcesService.merge(user_file_id, chunk_size, userFile.getSize(), userFile.getFile_name());
    }

    @Override
    public RestResult merge(Integer user_file_id, User user) {
        return merge(user_file_id, 5 * 1024 * 1024L, user);
    }

    @Override
    public Integer getFid(String filename, Long size, String dir, User user) {

        UserFile queryUserFile = new UserFile();
        queryUserFile.setUser(user);
        queryUserFile.setFile(new FileDB());
        queryUserFile.setFileType(FileType.FILE);
        queryUserFile.setDir(dir);
        queryUserFile.setFile_name(filename);

        List<UserFile> userFiles = userFileMapper.queryByUserFile(queryUserFile);

        if (!userFiles.isEmpty()) return 0;

        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setDelete(true);
        userFile.setFile(new FileDB());
        userFile.setGmt_create(new Date());
        userFile.setGmt_modified(new Date());
        userFile.setDir(dir);
        userFile.setSize(size);
        userFile.setFile_name(filename);
        userFileMapper.insertUserFile(userFile);
        return userFile.getId();
    }

    @Override
    public List<Map> getUploading(User user) {

        List<UserFile> uploading = userFileMapper.getUploading(user.getId());

        ArrayList<Map> maps = new ArrayList<>();

        for (UserFile userFile : uploading) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uf", userFile);
            map.put("tot_size", userFile.getSize());
            Long upload_chunk_size = chunkMapper.getChunkSize(userFile.getId());
            if (upload_chunk_size == null) upload_chunk_size = 0L;
            map.put("upload_chunk_size", upload_chunk_size);
            maps.add(map);
        }
        return maps;
    }
}
