package org.cloud.uploadanddownload;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.UserFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.cloud.uploadanddownload.mapper.ChunkMapper;
import org.cloud.uploadanddownload.mapper.UserFileMapper;
import org.cloud.uploadanddownload.service.ChunkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChunkTest {

    @Autowired
    ChunkService chunkService;

    @Autowired
    UserFileMapper userFileMapper;

    @Autowired
    ChunkMapper chunkMapper;

    @Test
    public void chunkUploadTest() throws Exception {
        String f = "/Users/crf/Downloads/视频.mp4";
        File file = new File(f);

        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
        try (InputStream input = new FileInputStream(file); OutputStream os = fileItem.getOutputStream();) {
            IOUtils.copy(input, os);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

            System.out.println(multipartFile.getOriginalFilename());
            System.out.println(multipartFile.getSize());

            int block_size = 5 * 1024 * 1024;
            Long block_tot = (multipartFile.getSize() + block_size - 1) / block_size;

            System.out.println("共" + block_tot + "块");

            for (int i = 0; i < block_tot ; i++) {
                chunkService.chunkUpload(multipartFile, 175, new User(), i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUploadingTest() {
        List<UserFile> uploading = userFileMapper.getUploading(1);
        System.out.println(uploading);
    }

    @Test
    public void getChunkSizeTest() {
        Long chunkSize = chunkMapper.getChunkSize(5);
        System.out.println(chunkSize);
    }

}
