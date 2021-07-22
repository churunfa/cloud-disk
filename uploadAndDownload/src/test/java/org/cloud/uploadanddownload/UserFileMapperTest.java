package org.cloud.uploadanddownload;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import org.cloud.uploadanddownload.mapper.UserFileMapper;
import org.cloud.uploadanddownload.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFileMapperTest {

    @Autowired
    UserFileMapper userFileMapper;

    @Autowired
    UserMapper userMapper;

    @Test
    public void queryBuUserFileTest() {
        UserFile userFile = new UserFile();
        userFile.setId(47);
        userFile.setUser(new User());
        userFile.setFile(new FileDB());
        List<UserFile> userFiles = userFileMapper.queryByUserFile(userFile);
        System.out.println(userFiles);
    }

    @Test
    public void updateSize() {
        Long l = userMapper.querySize(1);
        if (l == null) l = 0L;
        System.out.println(l);
        userMapper.updateUserSize(l, 1);
    }
}
