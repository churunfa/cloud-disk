package com.cloud.resources;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.resources.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileServiceTest {

    @Autowired
    FileService fileService;

    @Test
    public void moveFileTest() {
        String path1 = "/chunk-0/5/merge-a.mp4";

        FileDB fileDB = fileService.moveFile(path1);
        System.out.println(fileDB);

    }
}
