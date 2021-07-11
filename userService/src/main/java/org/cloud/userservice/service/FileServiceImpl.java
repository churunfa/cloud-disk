package org.cloud.userservice.service;

import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.FileType;
import com.cloud.common.pojo.file.UserFile;
import org.cloud.userservice.mapper.FileMapper;
import org.cloud.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileServiceImpl implements FileService{

    FileMapper fileMapper;

    UserMapper userMapper;

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PageBean<UserFile> getFileList(String dir, PageBean pageBean, User user) {
        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setFile(new FileDB());
        userFile.setDir(dir);


        int count = fileMapper.queryCountByUserFile(userFile);

        if (count == 0) {
            pageBean.setPageData(new ArrayList());
            return pageBean;
        }

        pageBean.setRecordCount(count);
        pageBean.setTotalPages((count + pageBean.getPageSize() - 1)/ pageBean.getPageSize());
        pageBean.init();

        if (pageBean.getPageNo() <= 0 || pageBean.getPageNo() > pageBean.getTotalPages()) return null;


        List<UserFile> userFiles = fileMapper.queryByUserFile(userFile, (pageBean.getPageNo() - 1) * pageBean.getPageSize(), pageBean.getPageSize());
        pageBean.setPageData(userFiles);
        return pageBean;
    }

    @Override
    public Map newDir(String path, String name, User user) {
        HashMap<String, Object> map = new HashMap<>();

        UserFile userFile1 = new UserFile();
        userFile1.setFile(new FileDB());
        userFile1.setUser(user);
        userFile1.setDir(path);
        userFile1.setFile_name(name);
        userFile1.setDelete(false);
        int count = fileMapper.queryCountByUserFile(userFile1);
        System.out.println("创建文件夹时查询到：" + count + "条结果");

        if (count != 0) {
            map.put("success", false);
            map.put("msg", "上传失败，当前文件夹下已经存在该文件");
            return map;
        }

        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setFile(new FileDB());
        userFile.setDir(path);
        userFile.setFileType(FileType.DIR);
        userFile.setGmt_create(new Date());
        userFile.setGmt_modified(new Date());
        userFile.setFile_name(name);
        System.out.println(userFile);
        int i = fileMapper.insertUserFile(userFile);
        if (i == 0) {
            map.put("success", false);
            map.put("msg", "插入数据库失败");
            return map;
        }

        map.put("success", true);
        map.put("msg", "创建成功");

        return map;
    }

    @Override
    public Map delete(Integer fileId, User user) {

        Map<String, Object> map = new HashMap<>();

        UserFile userFile = new UserFile();
        userFile.setId(fileId);
        userFile.setUser(user);
        userFile.setFile(new FileDB());

        List<UserFile> userFiles = fileMapper.queryAllUserFile(userFile);

        if (userFiles == null || userFiles.isEmpty()) {
            map.put("success", false);
            map.put("msg", "未找到文件");
            return map;
        }

        if (userFiles.size() != 1) {
            map.put("success", false);
            map.put("msg", "找到多个文件");
            return map;
        }

        userFile = userFiles.get(0);

        if (userFile.getFileType() == FileType.DIR) {

            String dir = userFile.getDir() + userFile.getFile_name() + "/";

            int count = fileMapper.updateDeleteByDir(user.getId(), dir, new Date());
            System.out.println("删除" + count + "个子项目");
        }

        UserFile updateUserFile = new UserFile();

        updateUserFile.setId(userFile.getId());
        updateUserFile.setUser(new User());
        updateUserFile.setFile(new FileDB());

        updateUserFile.setDelete(true);
        updateUserFile.setDelete_time(new Date());

        int i = fileMapper.updateUserFile(updateUserFile);

        Long userSize = userMapper.getUserSize(user.getId());
        userMapper.updateUserSize(userSize, user.getId());

        if (i != 0) {
            map.put("success", true);
            map.put("msg", "删除成功");
            return map;
        }

        map.put("success", false);
        map.put("msg", "删除失败");
        return map;
    }

    @Override
    public Map deletes(List<Integer> list, User user) {

        int tot = list.size();

        int count = 0;
        for (Integer integer : list) {
            Map delete = delete(integer, user);
            if ((Boolean)delete.get("success")) count++;
        }
        Map<String, Object> map = new HashMap<>();

        map.put("success", true);
        map.put("msg", "共选中" + tot + "个文件/文件夹，" + "成功删除" + count + "个");

        return map;
    }
}
