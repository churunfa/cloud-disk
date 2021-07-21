package com.cloud.resources;

import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.ChunkStatus;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import com.cloud.resources.mapper.ChunkMapper;
import com.cloud.resources.service.UploadService;
import com.cloud.resources.utils.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChunkTest {

    @Autowired
    ChunkMapper chunkMapper;

    @Autowired
    UploadService uploadService;

    @Test
    public void insertTest() {
        Chunk chunk = new Chunk();
        chunk.setFileDB(new FileDB());
        chunk.setUser_file_id(2);
        chunk.setGmt_create(new Date());
        chunk.setGmt_modified(new Date());
        chunk.setMd5("111");

        chunk.setPath(FileUtil.get("/chunk-0/2"));
        chunk.setName("1.zip");
        chunk.setChunk_number(1);
        chunk.setSize(1024L);

        chunkMapper.insert(chunk);
        System.out.println(chunk);

    }

    @Test
    public void updateTest() {
        Chunk chunk = new Chunk();
        chunk.setId(1);
        chunk.setFileDB(new FileDB());

        chunk.setSize(100L);
        chunkMapper.update(chunk);
    }

    @Test
    public void chunkUploadTest() throws IOException {
        String f = "/Users/crf/cloud-disk/resources/block-0/2.zip";
        File file = new File(f);

        FileItem fileItem = new DiskFileItem("multipartFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
        try (InputStream input = new FileInputStream(file); OutputStream os = fileItem.getOutputStream();) {
            IOUtils.copy(input, os);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

            Chunk chunk = uploadService.chunkUpload(multipartFile, 2, 1);
            System.out.println(chunk);
        }

    }

    @Test
    public void deleteByUserFileIdAndBlockTest() {
        chunkMapper.deleteByUserFileIdAndBlock(5, 66);
    }

    @Test
    public void merge() {
        FileDB merge = uploadService.merge(5, 5 * 1024 * 1024L, 349832005L, "a.mp4");
        System.out.println(merge);
    }

    @Test
    public void rev() {
        for (int i = 0; i <= 66; i++){
            Chunk chunk = chunkMapper.queryChunk(5, i);
            chunk.setFileDB(new FileDB());
            chunk.setStatus(ChunkStatus.PENDING);
            chunkMapper.update(chunk);
        }
    }

    @Test
    public void deleteTest() {
        String path = "/Users/crf/cloud-disk/resources/chunk-0/111";
        File file = new File(path);
        System.out.println(file);
        FileUtil.deleteFile(file);
    }
}
