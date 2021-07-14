package org.cloud.userservice;

import com.cloud.common.pojo.file.Recycle;
import com.cloud.common.pojo.file.UserFile;
import org.cloud.userservice.mapper.FileMapper;
import org.cloud.userservice.mapper.RecycleMapper;
import org.cloud.userservice.service.RecycleUtilsService;
import org.cloud.userservice.utils.DirUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecycleTest {

    @Autowired
    RecycleMapper recycleMapper;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    RecycleUtilsService recycleUtilsService;

    @Test
    public void insertRecycleTest() {
        Recycle recycle = new Recycle();
        UserFile userFile = new UserFile();
        userFile.setId(1);
        recycle.setUserFile(userFile);
        recycle.setRecycle_name("recycle");
        recycle.setRecycle_path("/");
        recycleMapper.insertRecycle(recycle);
    }

    @Test
    public void updateTest() {
        Recycle recycle = new Recycle();
        UserFile userFile = new UserFile();
        userFile.setId(1);

        recycle.setUserFile(userFile);

        recycle.setRecycle_name("abc");
        recycle.setRecycle_path("/abc/");

        recycleMapper.update(recycle);
    }

    @Test
    public void deleteTest() {

    }

    @Test
    public void queryByRecycleTest() {
        Recycle recycle = new Recycle();
        recycle.setUserFile(new UserFile());
        recycle.setRecycle_path("/abc");

        recycleMapper.queryByRecycle(recycle);
    }

    @Test
    public void replaceDirTest() {
        Recycle recycle = fileMapper.replaceDir(120, "/abc/", "/");
        System.out.println(recycle);
    }

    @Test
    public void getRecycleNameTest() {
        String recycleName = DirUtils.getRecycleName("abc.txt");
        System.out.println(recycleName);
    }

    @Test
    public void deleteUserFileTest() {
        UserFile userFile = fileMapper.queryUserFileById(120);
        Map map = recycleUtilsService.addRecycle(userFile, "/abc/", "/", true);

        System.out.println(map);

    }

    @Test
    public void queryListByDirTest() {
        List<Integer> integers = recycleMapper.queryListByDir(4, "/abc-2021-07-13 14:40:41 255");
        System.out.println(integers);
    }

}
