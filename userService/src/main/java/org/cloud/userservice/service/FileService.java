package org.cloud.userservice.service;

import com.cloud.common.pojo.PageBean;
import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.UserFile;

import java.util.List;
import java.util.Map;

public interface FileService {
    PageBean<UserFile> getFileList(String dir, PageBean pageBean, User user);
    Map newDir(String path, String name, User user);
    Map delete(Integer fileId, User user);
    Map deletes(List<Integer> list, User user);
}
