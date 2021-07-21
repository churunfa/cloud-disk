package com.cloud.resources;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.resources.mapper.FileMapper;
import com.cloud.resources.utils.FileUtil;
import com.cloud.resources.utils.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileSaveTest {

    @Autowired
    FileMapper fileMapper;

    @Test
    public void insertTest() {
        FileDB fileDB = new FileDB();
        fileDB.setFilename("abc2");
        fileDB.setMd5("abc");
        fileDB.setPath("/test");
        fileDB.setGmt_create(new Date());
        fileDB.setGmt_modified(new Date());
        int insert = fileMapper.insert(fileDB);
        System.out.println(insert);
        System.out.println(fileDB);
    }

    @Test
    public void updateTest() {
        FileDB fileDB = new FileDB();
        fileDB.setId(7);
        fileDB.setPath("/block-0");
        fileDB.setFilename("7.pdf");
        fileMapper.update(fileDB);
    }

    @Test
    public void check() throws IOException {
        String f1 = FileUtil.get("/block-0/9.xlsx");
        String f2 = FileUtil.get("/block-0/10.xlsx");
        Boolean flag = FileUtil.compareFile(f1, f2);
        System.out.println(flag);
    }
    @Test
    public void check2() throws IOException {
        String f1 = "/Users/crf/cloud-disk/resources/block-1/128.jpg";
        String f2 = "/Users/crf/cloud-disk/resources/block-1/108.jpg";
        Boolean flag = FileUtil.compareFile(f1, f2);
        System.out.println(flag);
    }

    @Test
    public void checkBig() throws IOException {
        String f1 = "/Users/crf/Documents/iso/cn_windows_10_consumer_editions_version_20h2_updated_dec_2020_x64_dvd_b7d6e09d.iso";
        String f2 = "/Users/crf/Documents/iso/cn_windows_10_consumer_editions_version_20h2_updated_dec_2020_x64_dvd_b7d6e09d.iso";

        Boolean flag = FileUtil.compareFile(f1, f2);
        System.out.println(flag);
    }
    @Test
    public void md5Big() throws IOException {
        String f = "/Users/crf/Documents/iso/cn_windows_10_consumer_editions_version_20h2_updated_dec_2020_x64_dvd_b7d6e09d.iso";

        String md5 = MD5.getMD5(new File(f));
        System.out.println(md5);
    }

}
