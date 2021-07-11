package org.cloud.uploadanddownload.service;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.FileType;
import com.cloud.common.pojo.file.UserFile;
import com.cloud.common.pojo.file.ZipFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloud.uploadanddownload.controller.ResourcesService;
import org.cloud.uploadanddownload.mapper.UserFileMapper;
import org.cloud.uploadanddownload.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserFileServiceImpl implements UserFileService {

    UserFileMapper userFileMapper;
    ResourcesService resourcesService;
    UserMapper userMapper;

    @Autowired
    public void setUserFileMapper(UserFileMapper userFileMapper) {
        this.userFileMapper = userFileMapper;
    }

    @Autowired
    public void setResourcesService(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public Map upload(MultipartFile file, User user, String dir) {
        Map<String, Object> map = new HashMap<>();
        if (user.getTotal_size() - user.getCount_size() < file.getSize()) {
            map.put("success", false);
            map.put("msg", "可用空间不足");
            return map;
        }
        System.out.println("-===========================-");

        System.out.println(file);

        UserFile paramUserFile = new UserFile();
        paramUserFile.setUser(user);
        paramUserFile.setFile(new FileDB());
        paramUserFile.setDir(dir);
        paramUserFile.setFileType(FileType.FILE);
        paramUserFile.setFile_name(file.getOriginalFilename());

        System.out.println(paramUserFile);
        System.out.println(file.getOriginalFilename());

        List<UserFile> userFiles = userFileMapper.queryByUserFile(paramUserFile);

        System.out.println(userFiles);

        if (!userFiles.isEmpty()) {
            map.put("success", false);
            map.put("msg", "当前目录下已有同名文件");
            return map;
        }

        Map msg = null;
        int i = 1;
        for (; i < 100 ; i++){
            msg = resourcesService.upload(file);
            if ((Boolean) msg.get("success")) break;

            String flag = (String) msg.get("msg");
            System.out.println(flag);
            System.out.println(!"file为空".equals(flag));
            System.out.println(flag == null);
            if (flag == null) break;
            if (!"file为空".equals(flag)) break;
        }
        System.out.println(file);
        System.out.println("上传次数" + i);
        System.out.println(msg);

        if (!(Boolean)msg.get("success")) {
            map.put("success", false);
            map.put("msg", "上传失败");
            return map;
        }

        FileDB fileDB = new ObjectMapper().convertValue(msg.get("msg"), FileDB.class);

        System.out.println(fileDB);

        UserFile userFile = new UserFile();
        userFile.setFile(fileDB);
        userFile.setUser(user);
        userFile.setFile_name(file.getOriginalFilename());
        userFile.setDir(dir);
        userFile.setFileType(FileType.FILE);
        userFile.setGmt_create(new Date());
        userFile.setGmt_modified(new Date());
        userFile.setSize(fileDB.getSize());

        int count = userFileMapper.insertUserFile(userFile);

        if (count == 0) {
            map.put("success", false);
            map.put("msg", "上传失败");
            return map;
        }


        User user1 = new User();
        user1.setId(user.getId());
        user1.setCount_size(user.getCount_size() + file.getSize());
        count = userMapper.updateUser(user1);

        if (count == 0) {
            map.put("success", false);
            map.put("msg", "上传失败");
            throw new RuntimeException();
        }

        map.put("success", true);
        map.put("msg", "上传成功");
        return map;
    }

    @Override
    public ResponseEntity<byte[]> downloads(ZipFile zipFile) {
        return resourcesService.downloads(zipFile);
    }

    @Override
    public ResponseEntity<byte[]> download(Integer id, String filename) {
        return resourcesService.download(id, filename);
    }
}
