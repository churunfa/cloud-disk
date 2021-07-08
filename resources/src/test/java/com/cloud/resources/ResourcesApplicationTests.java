package com.cloud.resources;

import com.cloud.common.pojo.file.ZipFile;
import com.cloud.resources.service.DownloadService;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.ZipUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourcesApplicationTests {

    @Test
    public void fileCopyTest() {
        String path = FileUtil.get("/abc/index.html");
        File cache = FileUtil.cache(new File(path),1 , "cache.html");
        System.out.println(cache.getName());
    }

    @Test
    public void refreshTest() {
        FileUtil.refresh(1);
    }

    @Autowired
    DownloadService downloadService;

    @Test
    public void DownloadServiceTest() {
        System.out.println(downloadService);
    }


    @Test
    public void zipTest() {
        String path = FileUtil.get("/tmp/1");
        ZipUtils.toZip_(path,  "test.zip");
    }

    @Test
    public void downloadByPathsTest() {

        ZipFile zipFile = new ZipFile();
        zipFile.setZipName("crf");

        String path = FileUtil.get("abc");

        HashMap<String, String> map = new HashMap<>();
        map.put(path + "/index.html", "1.html");
        map.put(path + "/a.txt", "2.txt");
        map.put(path + "/index.html", "1.html");

        zipFile.setFiles(map);

        ResponseEntity<byte[]> down = downloadService.downloadByPaths(zipFile);
        System.out.println(down);
    }

}
