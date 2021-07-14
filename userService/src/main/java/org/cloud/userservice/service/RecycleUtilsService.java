package org.cloud.userservice.service;

import com.cloud.common.pojo.file.Recycle;
import com.cloud.common.pojo.file.UserFile;
import org.cloud.userservice.mapper.FileMapper;
import org.cloud.userservice.mapper.RecycleMapper;
import org.cloud.userservice.utils.DirUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecycleUtilsService {

    private FileMapper fileMapper;
    private RecycleMapper recycleMapper;

    @Autowired
    public void setFileMapper(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Autowired
    public void setRecycleMapper(RecycleMapper recycleMapper) {
        this.recycleMapper = recycleMapper;
    }

    @Transactional
    public Map addRecycle(UserFile userFile, String oldDir, String newDir, boolean flag) {
        HashMap<String, Object> map = new HashMap<>();

        Recycle recycle = fileMapper.replaceDir(userFile.getId(), oldDir, newDir);
        recycle.setUserFile(userFile);

        String newName = DirUtils.getRecycleName(userFile.getFile_name());

        System.out.println(newName);

        if (flag) recycle.setRecycle_name(newName);
        else recycle.setRecycle_name(userFile.getFile_name());

        int count = recycleMapper.insertRecycle(recycle);

        if (count == 1) {
            int i = recycleMapper.deleteUserFile(userFile.getId(), new Date());
            if (i == 1) {
                map.put("success", true);
                map.put("msg", "更新recycle成功");
                if (flag) map.put("name", newName);
            } else  {
                map.put("success", false);
                map.put("msg", "更新recycle失败");
                new RuntimeException();
            }

        } else {
            map.put("success", false);
            map.put("msg", "更新recycle失败");
            new RuntimeException();
        }
        return map;
    }
}
