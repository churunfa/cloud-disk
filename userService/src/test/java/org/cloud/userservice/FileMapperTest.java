package org.cloud.userservice;


import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import org.cloud.userservice.mapper.FileMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileMapperTest {

    @Autowired
    FileMapper fileMapper;

    @Test
    public void queryBuUserFileTest() {
        UserFile userFile = new UserFile();
        userFile.setUser(new User());
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = fileMapper.queryByUserFile(userFile, 0, 10);
        System.out.println(userFiles);
    }
    @Test
    public void queryCountBuUserFileTest() {
        UserFile userFile = new UserFile();
        userFile.setUser(new User());
        userFile.setFile(new FileDB());

        int i = fileMapper.queryCountByUserFile(userFile);
        System.out.println(i);
    }
}
