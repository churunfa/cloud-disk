package org.cloud.userservice.service;

import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.FileType;
import com.cloud.common.pojo.file.Recycle;
import com.cloud.common.pojo.file.UserFile;
import org.cloud.userservice.mapper.FileMapper;
import org.cloud.userservice.mapper.RecycleMapper;
import org.cloud.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecycleServiceImpl implements RecycleService {

    RecycleMapper recycleMapper;
    FileMapper fileMapper;
    UserMapper userMapper;

    @Autowired
    public void setRecycleMapper(RecycleMapper recycleMapper) {
        this.recycleMapper = recycleMapper;
    }

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PageBean<UserFile> getFileList(String dir, PageBean<UserFile> pageBean, User user) {


//        List<Recycle> recycles = recycleMapper.queryUserFile(user.getId(), dir);

        int count = recycleMapper.queryDeleteCountUserFile(user.getId(), dir);

        if (count == 0) {
            pageBean.setPageData(new ArrayList());
            return pageBean;
        }

        pageBean.setRecordCount(count);
        pageBean.setTotalPages((count + pageBean.getPageSize() - 1)/ pageBean.getPageSize());
        pageBean.init();

        if (pageBean.getPageNo() <= 0 || pageBean.getPageNo() > pageBean.getTotalPages()) return null;

        List<Recycle> recycles = recycleMapper.queryUserFile(
                user.getId(),
                dir,
                (pageBean.getPageNo() - 1) * pageBean.getPageSize(),
                pageBean.getPageSize());

        ArrayList<UserFile> userFiles = new ArrayList<>();

        recycles.forEach((recycle)->{
            UserFile userFile = recycle.getUserFile();
            userFile.setFile_name(recycle.getRecycle_name());
            userFile.setDir(recycle.getRecycle_path());
            userFiles.add(recycle.getUserFile());
        });

        pageBean.setPageData(userFiles);
        return pageBean;
    }

    @Override
    @Transactional
    public Map delete(int id, User user) {

        Map<String, Object> map = new HashMap<>();

        UserFile userFile = fileMapper.queryUserFileById(id);
        if (userFile == null) {
            map.put("success", false);
            map.put("msg", "未找到文件或目录");
            return map;
        }

        Recycle recycle = new Recycle();
        recycle.setUserFile(userFile);

        List<Recycle> recycles = recycleMapper.queryByRecycle(recycle);

        if (recycles.size() != 1) {
            map.put("success", false);
            map.put("msg", "意外的查询结果");
            return map;
        }

        Recycle queryRecycle = recycles.get(0);
        queryRecycle.setUserFile(userFile);

        if (queryRecycle.getUserFile().getUser().getId() != user.getId()) {
            map.put("success", false);
            map.put("msg", "您无权限对此文件进行操作");
            return map;
        }

        if (queryRecycle.getUserFile().getFileType() == FileType.DIR) {
            String dir = queryRecycle.getRecycle_path() + queryRecycle.getRecycle_name();
            System.out.println("删除路径：" + dir);
            List<Integer> integers = recycleMapper.queryListByDir(user.getId(), dir);
            integers.forEach((fileId)->{
                recycleMapper.removeUserFile(fileId);
                recycleMapper.removeRecycle(fileId);
            });
        }

        recycleMapper.removeUserFile(queryRecycle.getUserFile().getId());
        recycleMapper.removeRecycle(queryRecycle.getUserFile().getId());

        map.put("success", true);
        map.put("msg", "删除成功");

        return map;
    }

    @Override
    public Map deletes(List<Integer> list, User user) {
        int tot = list.size();

        int count = 0;
        for (Integer integer : list) {
            Map delete = delete(integer, user);
            if ((Boolean) delete.get("success")) count++;
        }
        Map<String, Object> map = new HashMap<>();

        map.put("success", true);
        map.put("msg", "共选中" + tot + "个文件/文件夹，" + "成功删除" + count + "个");

        return map;
    }

    @Override
    @Transactional
    public Map recovery(int id, User user) {
        Map<String, Object> map = new HashMap<>();
        UserFile userFile = fileMapper.queryUserFileById(id);
        if (userFile.getUser().getId() != user.getId()) {
            map.put("success", false);
            map.put("msg", "不允许操作此资源");
            return map;
        }

        UserFile queryUserFile = new UserFile();
        queryUserFile.setUser(user);
        queryUserFile.setFile(new FileDB());
        queryUserFile.setDir(userFile.getDir());
        queryUserFile.setFile_name(userFile.getFile_name());
        queryUserFile.setFileType(userFile.getFileType());

        List<UserFile> userFiles = fileMapper.queryAllUserFile(queryUserFile);

        if (userFiles.size() != 0) {
            map.put("success", false);
            map.put("msg", "原目录下已有同名文件或文件夹");
            return map;
        }

        Recycle recycle = new Recycle();
        recycle.setUserFile(userFile);
        List<Recycle> recycles = recycleMapper.queryByRecycle(recycle);

        if (recycles.size() != 1) {
            map.put("success", false);
            map.put("msg", "意外的查询结果");
            return map;
        }

        Recycle recycle1 = recycles.get(0);

        if (!"/".equals(recycle1.getRecycle_path())) {
            map.put("success", false);
            map.put("msg", "只允许恢复跟路径下的文件！");
            return map;
        }

        if (userFile.getFileType() == FileType.DIR) {

            String dir = recycle1.getRecycle_path() + recycle1.getRecycle_name();
            System.out.println("恢复路径前缀：" + dir);
            List<Integer> integers = recycleMapper.queryListByDir(user.getId(), dir);

            integers.forEach((uFid)->{
                recycleMapper.removeRecycle(uFid);

                UserFile updateUserFile = new UserFile();
                updateUserFile.setId(uFid);
                updateUserFile.setUser(user);
                updateUserFile.setFile(new FileDB());
                updateUserFile.setDelete(false);
                fileMapper.updateUserFile(updateUserFile);

            });

        }

        recycleMapper.removeRecycle(userFile.getId());

        UserFile updateUserFile = new UserFile();
        updateUserFile.setId(userFile.getId());
        updateUserFile.setUser(user);
        updateUserFile.setFile(new FileDB());
        updateUserFile.setDelete(false);

        fileMapper.updateUserFile(updateUserFile);

        Long userSize = userMapper.getUserSize(user.getId());
        if (userSize == null) userSize = 0L;
        userMapper.updateUserSize(userSize, user.getId());

        map.put("success", true);
        map.put("msg", "恢复成功");

        return map;
    }

    @Override
    public Map recoveries(List<Integer> list, User user) {
        int tot = list.size();

        int count = 0;
        for (Integer integer : list) {
            Map recovery = recovery(integer, user);
            if ((Boolean) recovery.get("success")) count++;
        }
        Map<String, Object> map = new HashMap<>();

        map.put("success", true);
        map.put("msg", "共选中" + tot + "个文件/文件夹，" + "成功恢复" + count + "个");

        return map;
    }


}
